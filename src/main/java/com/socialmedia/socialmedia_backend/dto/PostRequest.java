package com.socialmedia.socialmedia_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotNull(message = "User id cannot be null")
    private Long userId;

    @NotNull(message = "imageUrl cannot be null")
    private String imageUrl;

    @NotBlank(message = "imageUrl cannot be blank")
    private String caption;
}
