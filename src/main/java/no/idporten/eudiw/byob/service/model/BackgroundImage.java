package no.idporten.eudiw.byob.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;

import java.net.URI;

public record BackgroundImage (
        @JsonProperty("uri")
        @Valid
        URI uri
) {
}
