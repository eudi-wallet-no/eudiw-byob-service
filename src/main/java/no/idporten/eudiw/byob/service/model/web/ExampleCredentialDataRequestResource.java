package no.idporten.eudiw.byob.service.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.idporten.eudiw.byob.service.model.ExampleCredentialData;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExampleCredentialDataRequestResource(
    @JsonProperty("name")
    String name,
    @JsonProperty("value")
    String value
){
    public ExampleCredentialData toExampleCredentialData() {
        return new ExampleCredentialData(
                this.name,
                this.value
        );
    }
}
