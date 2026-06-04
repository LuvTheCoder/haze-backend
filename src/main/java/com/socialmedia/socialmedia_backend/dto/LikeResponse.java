package com.socialmedia.socialmedia_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponse {
    private boolean liked;
    private long likesCount;
    private String message;
}
