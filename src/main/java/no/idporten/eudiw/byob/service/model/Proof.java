package no.idporten.eudiw.byob.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record Proof(
        @JsonProperty("credential_configuration")
        Map<String, CredentialMetadata> credentialConfiguration
) {

}
