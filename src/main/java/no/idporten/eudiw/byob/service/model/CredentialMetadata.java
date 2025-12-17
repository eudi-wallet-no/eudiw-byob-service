package no.idporten.eudiw.byob.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public record CredentialMetadata (
        @JsonProperty("doctype")
        String doctype,
        @JsonProperty("display")
        Display display,
        @JsonProperty("claims")
        Claims claims
) {
}
