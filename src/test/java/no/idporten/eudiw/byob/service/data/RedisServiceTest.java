package no.idporten.eudiw.byob.service.data;

import no.idporten.eudiw.byob.service.model.data.CredentialConfigurationData;
import no.idporten.eudiw.byob.service.model.data.CredentialMetadataData;
import no.idporten.eudiw.byob.service.model.data.ExampleCredentialDataData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DisplayName("Redis Service Test")
@ActiveProfiles("junit")
@SpringBootTest
@Disabled // this test runs now against actual redis on localhost, integration test to verify configuration. Todo write new unit-tests.
class RedisServiceTest {

    @Autowired
    private RedisConfig config;

    @Autowired
    private RedisService redisService;

    @Test
    public void testSaveBevistypeToRedis() {
        CredentialConfigurationData bevisType1 = new CredentialConfigurationData("test-vct-cred-id", "test-vct", "sd-jwt", new ExampleCredentialDataData("json"), new CredentialMetadataData(List.of(), List.of()));
        redisService.addBevisType(bevisType1);
    }
    @Test
    public void testGetAllFromRedis() {
        CredentialConfigurationData bevisType1 = new CredentialConfigurationData("test-vct-cred-id", "test-vct", "sd-jwt", new ExampleCredentialDataData("json"), new CredentialMetadataData(List.of(), List.of()));
        List<CredentialConfigurationData> all = redisService.getAll();
    }
}