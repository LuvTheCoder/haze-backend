package com.socialmedia.socialmedia_backend.dto;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String profilePicUrl;
    private String firstName;
    private String lastName;
    private String role;
    private boolean banned;
    private boolean verified;
    private LocalDateTime createdAt;
}
