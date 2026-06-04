package com.socialmedia.socialmedia_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "UserId cannot be NULL")
    private Long userId;
}
