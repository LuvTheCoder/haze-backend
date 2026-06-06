package com.socialmedia.socialmedia_backend.repository;

import com.socialmedia.socialmedia_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    List<User> findByUsernameContainingIgnoreCase(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM User u JOIN u.followers f WHERE u.id = :targetId AND f.id = :followerId")
    boolean isAlreadyFollowing(@Param("followerId") Long followerId,
                               @Param("targetId") Long targetId);

}
