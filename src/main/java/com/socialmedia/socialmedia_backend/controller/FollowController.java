package com.socialmedia.socialmedia_backend.controller;


import com.socialmedia.socialmedia_backend.entity.User;
import com.socialmedia.socialmedia_backend.repository.UserRepository;
import com.socialmedia.socialmedia_backend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final UserRepository userRepository;


    @PostMapping("/{targetUserId}")
    public ResponseEntity<String> toggleFollow(@PathVariable Long targetUserId, @AuthenticationPrincipal UserDetails userDetails){
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String result = followService.toggleFollow(currentUser.getId(), targetUserId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{userId}/followers-count")
    public ResponseEntity<Long> getFollowersCount(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowersCount(userId));
    }

    
    @GetMapping("/{userId}/following-count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowingCount(userId));
    }

    @GetMapping("/{targetUserId}/is-following")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable Long targetUserId,
            @RequestParam Long currentUserId) {
        return ResponseEntity.ok(followService.isFollowing(currentUserId, targetUserId));
    }
}
