package no.idporten.eudiw.byob.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Claims(
        @NotEmpty(message = "Claims path is null")
        @JsonProperty("path")
        String path,
        @JsonProperty("mandatory")
        boolean mandatory,
        @Valid
        @NotEmpty(message = "Claims display is null")
        @JsonProperty("display")
        List<Display> display
) {
}
