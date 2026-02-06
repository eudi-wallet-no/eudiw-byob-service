package no.idporten.eudiw.byob.service.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import no.idporten.eudiw.byob.service.model.Claims;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClaimsRequestResource(
        @NotBlank(message = "Claims path is null or blank")
        @JsonProperty("path")
        String path,
        @JsonProperty("type")
        String type,
        @JsonProperty("mime_type")
        String mimeType,
        @JsonProperty("mandatory")
        boolean mandatory,
        @Valid
        @NotEmpty(message = "Claims display is null or empty")
        @JsonProperty("display")
        List<@NotNull(message = "Claims display is null") DisplayRequestResource> display
) {

    public Claims toClaims() {
        return new Claims(
                this.path,
                this.type,
                this.mimeType,
                this.mandatory,
                this.display == null ? Collections.emptyList() : this.display.stream().map(DisplayRequestResource::toDisplay).toList()
        );
    }
}
