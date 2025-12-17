package no.idporten.eudiw.byob.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record Display(
        @NotBlank
        @JsonProperty("name")
        String name,
        @JsonProperty("description")
        String description,
        @JsonProperty("background_color")
        String backgroundColor,
        @JsonProperty("background_image")
        BackgroundImage backgroundImage,
        @JsonProperty("text_color")
        String textColor


) {
}
