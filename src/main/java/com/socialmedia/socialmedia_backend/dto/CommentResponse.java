package com.socialmedia.socialmedia_backend.dto;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private String content;
    private Long userId;
    private String username;
    private boolean userVerified;
    private String userProfilePic;
    private LocalDateTime createdAt;
}