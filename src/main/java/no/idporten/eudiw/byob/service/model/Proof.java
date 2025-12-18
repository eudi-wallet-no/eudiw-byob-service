package no.idporten.eudiw.byob.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;


@JsonIgnoreProperties(ignoreUnknown = true)
public record Proof(
        @JsonProperty("vct")
        @Pattern(regexp = "^[a-zA-Z0-9]$",
                message = "username must be of 6 to 12 length with no special characters")
        String vct,
        @JsonProperty("format")
        String format,
        @JsonProperty("example_credential_metadata")
        ExampleCredentialMetadata exampleCredentialMetadata,
        @JsonProperty("credential_metadata")
        CredentialMetadata credentialMetadata
) {
}
