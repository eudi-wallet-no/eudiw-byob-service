package no.idporten.eudiw.byob.service.serviceClasses;

import no.idporten.eudiw.byob.service.model.Proof;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CredentialConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(CredentialConfigurationService.class);
    private int counter;
    private static final String PREFIX = "net.eidas2sandkasse:";
    private static final String SD_JWT_VC = "_sd_jwt_vc";

    public CredentialConfigurationService() {
        this.counter = 0;
    }
    public String buildVct(List<String> registeredVcts, String vct) throws BadRequestException {
        if (vctAlreadyExists(registeredVcts, vct)) {
            log.info("Credential configuration already exists");
            throw new BadRequestException("invalid credential_configuration_id, already exists");
        }else {
            return PREFIX + vct + counter ++ + SD_JWT_VC;
        }
    }

    public boolean vctAlreadyExists(List<String> registeredVcts, String vct) {
        log.info("checking if vct exists: " + vct);
        return registeredVcts.contains(vct);
    }

    public Map<String, Proof> getResponseEntityMap(String id, Proof proof) {
        HashMap<String, Proof> proofMap = new HashMap<>();
        proofMap.put(id, proof);
        return proofMap;
    }
}
