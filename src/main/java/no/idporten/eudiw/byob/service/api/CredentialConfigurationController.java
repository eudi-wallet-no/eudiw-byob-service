package no.idporten.eudiw.byob.service.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import no.idporten.eudiw.byob.service.model.ByobInput;
import no.idporten.eudiw.byob.service.model.ResponseTopObject;
import no.idporten.eudiw.byob.service.serviceClasses.CredentialConfigurationService;
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
            summary = "Lag en n bevistype",
            description = "Her kan du registrere en ny bevistype for å utforske bevisutstedelse på en enkel og rask måte.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bevistypen ble laget",
                    content = @Content(mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(examples= @ExampleObject(description = "Intern feil", value = ByobServiceAPISwaggerExamples.SERVER_ERROR_EXAMPLE)))
    })


    @PostMapping(path = "/v1/credential-configurations", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, ByobInput>> createCredentialConfiguration(@Valid @RequestBody ByobInput proof){
        return ResponseEntity.ok(service.getResponseModel(proof));
    }

    @Operation(
            summary = "Hente alle bevistyper som er laget med BYOB-en (Bring Your Own Bevis)",
            description = "Hent alle bevistypene som er laget via dette API-et.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alle bevistypene fra BYOB hentes"),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(examples= @ExampleObject(description = "Intern feil", value = ByobServiceAPISwaggerExamples.SERVER_ERROR_EXAMPLE)))
    })

    @GetMapping(value = "/v1/credential-configurations", produces =  MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<ResponseTopObject> retrieveAllCredentialConfigurations() {
        return ResponseEntity.ok(service.getAllEntries());
    }

    @Operation(
            summary = "Hente en enkelt bevistype med gitt id",
            description = "Gitt insatt vct som stiparameter, så henter den registrert bevistype dersom denne eksisterer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bevistype med matchende VCT hentes fra lagring"),
            @ApiResponse(responseCode = "500", description = "Intern feil"),
            @ApiResponse(responseCode = "404", description = "Fant ingen bevis med gitt VCT",
                    content = @Content(examples= @ExampleObject(description = "Ikke funnet", value = ByobServiceAPISwaggerExamples.NOT_FOUND)))
    })

    @GetMapping(value = "/v1/credential-configurations/{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<ByobInput> retrieveSelectedCredentialConfiguration(@PathVariable String id) {
        return ResponseEntity.ofNullable(service.searchCredentialConfiguration(id));
    }
}
