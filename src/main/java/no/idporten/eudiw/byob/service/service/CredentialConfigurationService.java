package no.idporten.eudiw.byob.service.service;

import no.idporten.eudiw.byob.service.exception.BadRequestException;
import no.idporten.eudiw.byob.service.model.ByobRequest;
import no.idporten.eudiw.byob.service.model.ByobResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CredentialConfigurationService {

    private Map<String, ByobRequest> persistenceLayer = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(CredentialConfigurationService.class);
    private int counter= 0;
    private static final String PREFIX = "net.eidas2sandkasse:";
    private static final String SD_JWT_VC = "_sd_jwt_vc";

    public CredentialConfigurationService() {

    }

    /**
     * Takes in user input, sends input to check if the VCT is already registered, and if
     * not already registered, it gives back a generated id which is not used for anything
     * as well as the proof.
     *
     * @param proof user input that user POSTS in to BYOB in order to "build your own bevis"
     * @return a new HashMap with an id that consists of a set prefix, the VCT given in input,
     *  a counter and format.
     */
    public Map<String, ByobRequest> getResponseModel(ByobRequest proof) {
        String id = buildVct(proof);
        HashMap<String, ByobRequest> response = new HashMap<>();
        response.put(id, proof);
        return response;
    }

    public String buildVct(ByobRequest proof) {
        if (persistenceLayer.containsKey(proof.vct())) {
            throw new BadRequestException("Credential configuration already exists");
        }
            updatePersistenceLayer(proof);
            return PREFIX + proof.vct() + counter++ + SD_JWT_VC;
    }

    public void updatePersistenceLayer(ByobRequest proof) {
        this.persistenceLayer.put(proof.vct(), proof);
    }

    public ByobResponse getAllEntries() {
        return new ByobResponse(persistenceLayer.values().stream().toList());

    }

    public ByobRequest searchCredentialConfiguration(String id) {
        return persistenceLayer.get(id);
    }
}
