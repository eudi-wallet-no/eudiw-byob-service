package no.idporten.eudiw.byob.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record Claims(
        @NotNull
        @JsonProperty("path")
        List<@NotBlank String> path,
        @JsonProperty("mandatory")
        boolean mandatory,
        @JsonProperty("display")
        List<Display> display
) {
}
