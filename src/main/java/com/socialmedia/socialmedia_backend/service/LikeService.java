package com.socialmedia.socialmedia_backend.service;

import com.socialmedia.socialmedia_backend.dto.LikeResponse;
import com.socialmedia.socialmedia_backend.entity.Like;
import com.socialmedia.socialmedia_backend.entity.Post;
import com.socialmedia.socialmedia_backend.entity.User;
import com.socialmedia.socialmedia_backend.exception.ResourceNotFoundException;
import com.socialmedia.socialmedia_backend.repository.LikeRepository;
import com.socialmedia.socialmedia_backend.repository.PostRepository;
import com.socialmedia.socialmedia_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikeResponse toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        boolean liked;
        String message;

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            liked = false;
            message = "Post unliked successfully";
        } else {
            Like like = Like.builder()
                    .user(user)
                    .post(post)
                    .build();
            likeRepository.save(like);
            liked = true;
            message = "Post liked successfully";
        }

        long count = likeRepository.countByPost(post);

        return LikeResponse.builder()
                .liked(liked)
                .likesCount(count)
                .message(message)
                .build();
    }

    public boolean isPostLikedByUser(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        return likeRepository.existsByUserAndPost(user, post);
    }

    public long getLikesCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        return likeRepository.countByPost(post);
    }
}
