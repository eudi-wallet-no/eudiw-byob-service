package no.idporten.eudiw.byob.service.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import no.idporten.eudiw.byob.service.model.ByobRequest;
import no.idporten.eudiw.byob.service.model.ByobResponse;
import no.idporten.eudiw.byob.service.service.CredentialConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CredentialConfigurationController {

    CredentialConfigurationService service;

    @Autowired
    public CredentialConfigurationController(CredentialConfigurationService service) {
        this.service = service;

    }

    @Operation(
            summary = "Lag et nytt bevis via BYOB utstederen",
            description = "Her kan du registrere bevis for å utforske bevisutstedelse på en enkel og rask måte.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Beviset ble laget",
                    content = @Content(mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(examples= @ExampleObject(description = "Intern feil", value = ByobServiceAPISwaggerExamples.SERVER_ERROR_EXAMPLE)))
    })


    @PostMapping(path = "/v1/credential-configurations", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, ByobRequest>> createCredentialConfiguration(@Valid @RequestBody ByobRequest proof){
        return ResponseEntity.ok(service.getResponseModel(proof));
    }

    @Operation(
            summary = "Hente alle bevis som er laget med BYOB-en (Bring Your Own Bevis)",
            description = "Hent alle bevisene som er laget ved hjelp av BYOB-utstederen.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alle bevis utstedt av BYOB-utstederen hentes"),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(examples= @ExampleObject(description = "Intern feil", value = ByobServiceAPISwaggerExamples.SERVER_ERROR_EXAMPLE)))
    })

    @GetMapping(value = "/v1/credential-configurations", produces =  MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<ByobResponse> retrieveAllCredentialConfigurations() {
        return ResponseEntity.ok(service.getAllEntries());
    }

    @Operation(
            summary = "Hente et enkelt bevis registrert av BYOB-utstederen",
            description = "Søk i URL-en med stiparameter som er VCT-en til ønsket bevis.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Beviset med matchende VCT hentes fra lagring"),
            @ApiResponse(responseCode = "500", description = "Intern feil"),
            @ApiResponse(responseCode = "404", description = "Fant ingen bevis med gitt VCT",
                    content = @Content(examples= @ExampleObject(description = "Brukerfeil", value = ByobServiceAPISwaggerExamples.NOT_FOUND)))
    })

    @GetMapping(value = "/v1/credential-configurations/{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<ByobRequest> retrieveSelectedCredentialConfiguration(@PathVariable String id) {
        return ResponseEntity.ofNullable(service.searchCredentialConfiguration(id));
    }
}
