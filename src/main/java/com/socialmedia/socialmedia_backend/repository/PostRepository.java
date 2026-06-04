package com.socialmedia.socialmedia_backend.repository;

import com.socialmedia.socialmedia_backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
