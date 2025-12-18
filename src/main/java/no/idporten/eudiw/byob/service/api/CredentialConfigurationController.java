package no.idporten.eudiw.byob.service.api;

import jakarta.validation.Valid;
import no.idporten.eudiw.byob.service.model.Proof;
import no.idporten.eudiw.byob.service.serviceClasses.CredentialConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class CredentialConfigurationController {

    CredentialConfigurationService service;

    @Autowired
    public CredentialConfigurationController(CredentialConfigurationService service) {
        this.service = service;

    }

    private Map<String, Proof> persistenceLayer = new HashMap<>();


    @PostMapping(path = "/v1/credential-configurations", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Proof>> createCredentialConfiguration(@Valid @RequestBody Proof proof){
        String id = service.buildVct(persistenceLayer.keySet().stream().toList(), proof.vct());
        this.persistenceLayer.put(proof.vct(), proof);
        return ResponseEntity.ok(service.getResponseEntityMap(id, proof));
    }

    @GetMapping(value = "/v1/credential-configurations", produces =  MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<List<Proof>> retrieveAllCredentialConfigurations() {
        return ResponseEntity.ok(persistenceLayer.values().stream().toList());
    }

}
