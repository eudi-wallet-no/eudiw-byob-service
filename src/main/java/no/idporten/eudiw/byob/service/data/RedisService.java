package no.idporten.eudiw.byob.service.data;

import jakarta.annotation.PostConstruct;
import no.idporten.eudiw.byob.service.model.CredentialConfiguration;
import no.idporten.eudiw.byob.service.model.data.CredentialConfigurationData;
import no.idporten.eudiw.byob.service.service.MockCredentialConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RedisService {

    private final Logger log = LoggerFactory.getLogger(RedisService.class);

    public static final String DATA_PREFIX_BYOB_TYPES = "byob-service:credential-configuration-types:"; // Key-data map (vct -> credential-configuration)
    public static final String KEY_PREFIX_BYOB_ID = "byob-service:credential-types:"; // Key mapping (credential-configuration-id -> vct). Can use to lookup data (credential-configuration by vct) afterwards.

    private final RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
        Set<String> allKeys = getAllKeys();
        log.info("RedisService initialized. Found existing keys with prefix {} in Redis: {}", DATA_PREFIX_BYOB_TYPES, allKeys);
        addMockedCredentialConfigurationsToRedis();
    }

    // remove me when mocked data is no longer needed
    private void addMockedCredentialConfigurationsToRedis() {
        List<CredentialConfiguration> credentialConfigurationsMocked = MockCredentialConfigurations.getCredentialConfigurationsListMocked();
        for (CredentialConfiguration credentialConfiguration : credentialConfigurationsMocked) {
            CredentialConfigurationData bevisTypeData = new CredentialConfigurationData(credentialConfiguration);
            addBevisType(bevisTypeData);
            log.info("Added mocked BevisType to Redis: vct={}", credentialConfiguration.vct()); // will override or not add if already exists
        }
        log.info("RedisService initialization complete. Total BevisTypes in Redis: {}", getAll().size());
    }

    public void addBevisType(CredentialConfigurationData bevisType) {
        valueOperations.set(DATA_PREFIX_BYOB_TYPES + bevisType.vct(), bevisType);
        valueOperations.set(KEY_PREFIX_BYOB_ID + bevisType.credentialConfigurationId(), bevisType.vct());
        log.info("Added BevisType to Redis: vct={}", bevisType.vct());
    }

    public CredentialConfigurationData getBevisType(String vct) {
        return (CredentialConfigurationData) valueOperations.get(DATA_PREFIX_BYOB_TYPES + vct);
    }

    public CredentialConfigurationData getBevisTypeByCredentialConfiguration(String credentialConfigurationId) {
        String vct = (String) valueOperations.get(KEY_PREFIX_BYOB_ID + credentialConfigurationId);
        return (CredentialConfigurationData) valueOperations.get(DATA_PREFIX_BYOB_TYPES + vct);
    }

    // TODO: Performance improvement: possible to fetch all values without fetching each individually by key?
    public List<CredentialConfigurationData> getAll() {
        Set<String> keys = getAllKeys();
        List<CredentialConfigurationData> all = new ArrayList<>();
        for (String key : keys) {
            System.out.println("redis key=" + key);
            valueOperations.get(key);
            all.add((CredentialConfigurationData) valueOperations.get(key));
        }
        return all;
    }

    private Set<String> getAllKeys() {
        return redisTemplate.keys(DATA_PREFIX_BYOB_TYPES + "*");
    }

}
