package no.idporten.eudiw.byob.service.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


@JsonIgnoreProperties(ignoreUnknown = true)
public record CredentialConfigurationRequestResource(

        @JsonProperty("credential_type")
        @Pattern(regexp = "^[a-z0-9_:.]{3,155}$",
                message = "Credential type must be lowercase letters/numbers, underscores or colons, length 3-155")
        String credentialType,

        @JsonProperty("format")
        @Pattern(regexp = "dc\\+sd-jwt|mso_mdoc", message = "Format must be dc+sd-jwt or mso_mdoc")
        String format,

        @JsonProperty("scope")
        @Pattern(regexp = "^[a-zæøå][a-zæøå0-9+\\-.]*:([a-zæøå0-9]+/?)+(:[a-zæøå0-9]+)*[a-zæøå0-9]+(\\.[a-zæøå0-9]+)*$", message = "Scope must be on format prefix:sub_scope") // fra https://github.com/felleslosninger/kut-selvbetjening-api/blob/main/src/main/java/no/digdir/kundetjenester/selvbetjening/api/scope/ScopeValidator.java
        String scope,

        @Valid
        @JsonProperty("example_credential_data")
        ExampleCredentialDataRequestResource exampleCredentialData,

        @Valid
        @JsonProperty("credential_metadata")
        @NotNull(message = "credential_metadata is null")
        CredentialMetadataRequestResource credentialMetadata
) {
}
