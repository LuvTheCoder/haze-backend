package com.socialmedia.socialmedia_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCaptionRequest {

    @NotBlank(message = "Caption is required")
    private String caption;
}
