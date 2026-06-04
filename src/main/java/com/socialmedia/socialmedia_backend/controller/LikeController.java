package com.socialmedia.socialmedia_backend.controller;

import com.socialmedia.socialmedia_backend.dto.LikeResponse;
import com.socialmedia.socialmedia_backend.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/post/{postId}/toggle")
    public ResponseEntity<LikeResponse> toggleLike(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        LikeResponse response = likeService.toggleLike(postId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}/liked")
    public ResponseEntity<Boolean> isPostLikedByUser(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        boolean liked = likeService.isPostLikedByUser(postId, userId);
        return ResponseEntity.ok(liked);
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long postId) {
        long count = likeService.getLikesCount(postId);
        return ResponseEntity.ok(count);
    }
}
