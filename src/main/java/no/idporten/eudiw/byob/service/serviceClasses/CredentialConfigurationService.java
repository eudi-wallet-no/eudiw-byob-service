package no.idporten.eudiw.byob.service.serviceClasses;

import no.idporten.eudiw.byob.service.exception.BadRequestException;
import no.idporten.eudiw.byob.service.model.ByobInput;
import no.idporten.eudiw.byob.service.model.ResponseTopObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CredentialConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(CredentialConfigurationService.class);
    private int counter= 0;
    private static final String PREFIX = "net.eidas2sandkasse:";
    private static final String SD_JWT_VC = "_sd_jwt_vc";

    public CredentialConfigurationService() {

    }
    public String buildVct(List<String> registeredVcts, String vct) {
        if (vctAlreadyExists(registeredVcts, vct)) {
            throw new BadRequestException("Credential configuration already exists");
        }else {
            return PREFIX + vct + counter++ + SD_JWT_VC;
        }
    }

    public boolean vctAlreadyExists(List<String> registeredVcts, String vct) {
        return registeredVcts.contains(vct);
    }

    public Map<String, ByobInput> getResponseModel(String credential_configuration, ByobInput proof) {
        HashMap<String, ByobInput> proofMap = new HashMap<>();
        proofMap.put(credential_configuration, proof);
        return proofMap;
    }

    public ResponseTopObject prepareResponse(Map<String, ByobInput> entries) {
    return new ResponseTopObject(entries.values().stream().toList());
    }
}
