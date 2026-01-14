package no.idporten.eudiw.byob.service.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.idporten.eudiw.byob.service.model.CredentialConfiguration;

import java.io.Serializable;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record CredentialConfigurationData(

        @JsonProperty("credential_configuration_id")
        String credentialConfigurationId,

        @JsonProperty("vct")
        String vct,

        @JsonProperty("format")
        String format,

        @JsonProperty("example_credential_data")
        List<ExampleCredentialDataData> exampleCredentialData,

        @JsonProperty("credential_metadata")
        CredentialMetadataData credentialMetadata
) implements Serializable {

    public CredentialConfigurationData(CredentialConfiguration other) {
        this(
                other.credentialConfigurationId(),
                other.vct(),
                other.format(),
                other.exampleCredentialData().stream().map(example -> new ExampleCredentialDataData(example.name(), example.value())).toList(),
                new CredentialMetadataData(other.credentialMetadata())
        );
    }

    public CredentialConfiguration toCredentialConfiguration() {
        return new CredentialConfiguration(
                this.credentialConfigurationId,
                this.vct,
                this.format,
                this.exampleCredentialData().stream().map(ExampleCredentialDataData::toExampleCredentialData).toList(),
                this.credentialMetadata.toCredentialMetadata()
        );
    }
}
