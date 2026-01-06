package no.idporten.eudiw.byob.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ByobResponse(
        @JsonProperty("credential_configurations")
        List<ByobRequest> credentialConfigurations
) {
}
