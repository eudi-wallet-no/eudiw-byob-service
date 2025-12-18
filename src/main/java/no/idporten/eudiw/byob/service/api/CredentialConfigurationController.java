package no.idporten.eudiw.byob.service.api;

import no.idporten.eudiw.byob.service.model.Proof;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class CredentialConfigurationController {

    private static final Logger log = LoggerFactory.getLogger(CredentialConfigurationController.class);
    private Map<String, Proof> persistenceLayer = new HashMap<>();


    @PostMapping(path = "/v1/credential-configurations", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Proof> createCredentialConfiguration(@RequestBody Proof proof) {
        this.persistenceLayer.put(proof.vct(), proof);
        return ResponseEntity.ok(persistenceLayer.get(proof.vct()));
    }

    @GetMapping(value = "/v1/credential-configurations", produces =  MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<List<Proof>> retrieveAllCredentialConfigurations() {
        return ResponseEntity.ok(persistenceLayer.values().stream().toList());
    }

}
