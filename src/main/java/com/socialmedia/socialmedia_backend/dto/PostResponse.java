package com.socialmedia.socialmedia_backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String caption;
    private String imageUrl;
    private Long userId;
    private String username;
    private boolean userVerified;
    private LocalDateTime createdAt;
    private long likesCount;
}