package com.socialmedia.socialmedia_backend.controller;


import com.socialmedia.socialmedia_backend.dto.PostRequest;
import com.socialmedia.socialmedia_backend.dto.PostResponse;
import com.socialmedia.socialmedia_backend.dto.UpdateCaptionRequest;
import com.socialmedia.socialmedia_backend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest){
        PostResponse response = postService.createPost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> responses = postService.getAllPosts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable Long userId) {
        List<PostResponse> responses = postService.getPostsByUser(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/caption")
    public ResponseEntity<PostResponse> updateCaption(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCaptionRequest request) {
        PostResponse response = postService.updateCaption(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
