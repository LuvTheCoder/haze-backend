package com.socialmedia.socialmedia_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;

    private String firstName;
    private String lastName;

    @Email(message = "Enter valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String bio;

    private String profilePicUrl;

    @Column(nullable = false)
    @Builder.Default
    private String role = "USER";

    @Column(nullable = false)
    @Builder.Default
    private boolean banned = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean verified = false;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private List<Like> likes;

    @ManyToMany
    @JoinTable(
          name="user_followers",
          joinColumns = @JoinColumn(name="following_id"),
          inverseJoinColumns = @JoinColumn(name="follower_id")
    )
    @JsonIgnoreProperties({"followers", "following", "posts", "comments", "likes", "password"})
    private List<User> followers = new ArrayList<>();

    @ManyToMany(mappedBy = "followers")
    @JsonIgnoreProperties({"followers", "following", "posts", "comments", "likes", "password"})
    private  List<User> following = new ArrayList<>();

    @PrePersist
    public void createdTime(){
        this.createdAt = LocalDateTime.now();
    }



}
