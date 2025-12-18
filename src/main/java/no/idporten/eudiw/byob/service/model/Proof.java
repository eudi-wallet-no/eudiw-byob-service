package no.idporten.eudiw.byob.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public record Proof(
        @JsonProperty("vct")
        String vct,
        @JsonProperty("format")
        String format,
        @JsonProperty("example_credential_metadata")
        ExampleCredentialMetadata exampleCredentialMetadata,
        @JsonProperty("credential_metadata")
        CredentialMetadata credentialMetadata
) {
}
