package com.socialmedia.socialmedia_backend.service;

import com.socialmedia.socialmedia_backend.dto.PostRequest;
import com.socialmedia.socialmedia_backend.dto.PostResponse;
import com.socialmedia.socialmedia_backend.dto.UpdateCaptionRequest;
import com.socialmedia.socialmedia_backend.entity.Post;
import com.socialmedia.socialmedia_backend.entity.User;
import com.socialmedia.socialmedia_backend.exception.ResourceNotFoundException;
import com.socialmedia.socialmedia_backend.repository.PostRepository;
import com.socialmedia.socialmedia_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse createPost(PostRequest postRequest){
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(()->new EntityNotFoundException("User not found with id " + postRequest.getUserId()));

        Post post = new Post();
        post.setCaption(postRequest.getCaption());
        post.setImageUrl(postRequest.getImageUrl());
        post.setCaption(postRequest.getCaption());

        post.setUser(user);

        Post saved = postRepository.save(post);
        return mapToResponse(saved);
    }

    public List<PostResponse> getAllPosts(){
        List<Post> posts = postRepository.findAll();
        List<PostResponse> responses = new ArrayList<>();
        for(Post post : posts){
            responses.add(mapToResponse(post));
        }

        return responses;
    }

    @Transactional
    public List<PostResponse> getPostsByUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User not found with id "+userId));
        List<Post> posts = user.getPosts();
        List<PostResponse> responses = new ArrayList<>();
        for(Post post : posts){
            responses.add(mapToResponse(post));
        }

        return responses;
    }

    public PostResponse updateCaption(Long postId, UpdateCaptionRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        post.setCaption(request.getCaption());

        Post updated = postRepository.save(post);

        return mapToResponse(updated);
    }

    public void deletePost(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        postRepository.deleteById(postId);
    }


    public PostResponse mapToResponse(Post post){
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setCaption(post.getCaption());
        response.setImageUrl(post.getImageUrl());
        response.setUserId(post.getUser().getId());
        response.setUsername(post.getUser().getUsername());
        response.setUserVerified(post.getUser().isVerified());
        response.setCreatedAt(post.getCreatedAt());
        response.setLikesCount(post.getLikes() != null ? post.getLikes().size() : 0);

        return response;
    }


}
