package com.socialmedia.socialmedia_backend.repository;

import com.socialmedia.socialmedia_backend.entity.Like;
import com.socialmedia.socialmedia_backend.entity.Post;
import com.socialmedia.socialmedia_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(User user, Post post);
    
    boolean existsByUserAndPost(User user, Post post);
    long countByPost(Post post);
}
