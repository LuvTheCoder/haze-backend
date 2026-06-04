package com.socialmedia.socialmedia_backend.service;

import com.socialmedia.socialmedia_backend.dto.CommentRequest;
import com.socialmedia.socialmedia_backend.dto.CommentResponse;
import com.socialmedia.socialmedia_backend.entity.Comment;
import com.socialmedia.socialmedia_backend.entity.Post;
import com.socialmedia.socialmedia_backend.entity.User;
import com.socialmedia.socialmedia_backend.exception.ResourceNotFoundException;
import com.socialmedia.socialmedia_backend.repository.CommentRepository;
import com.socialmedia.socialmedia_backend.repository.PostRepository;
import com.socialmedia.socialmedia_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse addComment(Long postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
        List<CommentResponse> responses = new ArrayList<>();
        for (Comment comment : comments) {
            responses.add(mapToResponse(comment));
        }
        return responses;
    }

    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setUserId(comment.getUser().getId());
        response.setUsername(comment.getUser().getUsername());
        response.setUserVerified(comment.getUser().isVerified());
        response.setUserProfilePic(comment.getUser().getProfilePicUrl());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }
}
