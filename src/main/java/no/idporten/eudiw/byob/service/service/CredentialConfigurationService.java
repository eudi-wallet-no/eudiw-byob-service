package no.idporten.eudiw.byob.service.service;

import no.idporten.eudiw.byob.service.data.RedisService;
import no.idporten.eudiw.byob.service.exception.BadRequestException;
import no.idporten.eudiw.byob.service.model.CredentialConfiguration;
import no.idporten.eudiw.byob.service.model.CredentialConfigurations;
import no.idporten.eudiw.byob.service.model.CredentialMetadata;
import no.idporten.eudiw.byob.service.model.ExampleCredentialData;
import no.idporten.eudiw.byob.service.model.data.CredentialConfigurationData;
import no.idporten.eudiw.byob.service.model.web.CredentialConfigurationRequestResource;
import no.idporten.eudiw.byob.service.model.web.ExampleCredentialDataRequestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CredentialConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(CredentialConfigurationService.class);

    public static final String CREDENTIAL_TYPE_PREFIX = "net.eidas2sandkasse:";
    private static final String SD_JWT_VC = "_sd_jwt_vc";

    private final RedisService redisService;

    @Autowired
    public CredentialConfigurationService(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * Takes in user input, sends input to check if the credential type is already registered, and if
     * not already registered, it gives back a generated id which is not used for anything
     * as well as the credentialConfiguration.
     *
     * @param credentialConfiguration user input that user POSTS in to BYOB in order to "build your own bevis"
     * @return a new CredentialConfiguration with an id that consists of a set prefix, the credential type given in input plus format.
     */
    public CredentialConfiguration create(CredentialConfigurationRequestResource credentialConfiguration) {
        String credentialType = credentialConfiguration.credentialType();
        if (!credentialConfiguration.credentialType().startsWith(CREDENTIAL_TYPE_PREFIX)) {
            credentialType = CREDENTIAL_TYPE_PREFIX + credentialType;
        }
        if (redisService.getBevisType(credentialType) != null) {
            throw new BadRequestException("Credential-configuration already exists for credentialType=%s".formatted(credentialConfiguration.credentialType()));
        }
        String credentialConfigurationId = CREDENTIAL_TYPE_PREFIX + credentialConfiguration.credentialType() + SD_JWT_VC;
        CredentialConfiguration cc = convert(credentialConfiguration, credentialConfigurationId, credentialType);
        redisService.addBevisType(new CredentialConfigurationData(cc));
        log.info("Generated new credential-configuration for credentialType: {}", cc.credentialType());
        return cc;
    }

    private static CredentialConfiguration convert(CredentialConfigurationRequestResource credentialConfiguration, String credentialConfigurationId, String credentialType) {
        ExampleCredentialData exampleCredentialData = convertExampleData(credentialConfiguration.exampleCredentialData());
        CredentialMetadata credentialMetadata = credentialConfiguration.credentialMetadata().toCredentialMetadata();
        return new CredentialConfiguration(
                credentialConfigurationId,
                credentialType,
                credentialConfiguration.format(),
                credentialConfiguration.scope(),
                exampleCredentialData,
                credentialMetadata
        );
    }

    private static ExampleCredentialData convertExampleData(ExampleCredentialDataRequestResource exampleCredentialData) {
        if(CollectionUtils.isEmpty(exampleCredentialData)) {
            return new ExampleCredentialData();
        }
        return new ExampleCredentialData(exampleCredentialData);
    }

    public CredentialConfigurations getAllEntries() {
        List<CredentialConfigurationData> all = redisService.getAll();
        List<CredentialConfiguration> list = all.stream().map(CredentialConfigurationData::toCredentialConfiguration).toList();
        return new CredentialConfigurations(list);
    }

    public CredentialConfiguration getCredentialConfiguration(String credentialType) {
        CredentialConfigurationData data = redisService.getBevisType(credentialType);
        if (data == null) { return null;}
        return data.toCredentialConfiguration();
    }

    public CredentialConfiguration searchCredentialConfiguration(String credentialConfigurationId) {
        CredentialConfigurationData data = redisService.getBevisTypeByCredentialConfiguration(credentialConfigurationId);
        if (data == null) { return null;}
        return data.toCredentialConfiguration();
    }

    public void delete(String credentialType) {
         redisService.delete(credentialType);
    }

    public void deleteAll() {
        redisService.deleteAll();
    }

    public CredentialConfiguration update(CredentialConfigurationRequestResource credentialConfiguration) {
        String credentialType = credentialConfiguration.credentialType();
        CredentialConfigurationData oldBevisType = redisService.getBevisType(credentialType);
        if (oldBevisType == null) {
            throw new BadRequestException("Credential-configuration not created for credentialType=%s. Create bevisType before update.".formatted(credentialConfiguration.credentialType()));
        }
        CredentialConfiguration cc = convert(credentialConfiguration, oldBevisType.credentialConfigurationId(), credentialType);
        redisService.updateBevisType(new CredentialConfigurationData(cc));
        log.info("Updated credential-configuration for credentialType: {}", cc.credentialType());
        return cc;
    }
}
