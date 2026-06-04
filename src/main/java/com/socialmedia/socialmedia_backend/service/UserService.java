package com.socialmedia.socialmedia_backend.service;

import com.socialmedia.socialmedia_backend.dto.UserRequest;
import com.socialmedia.socialmedia_backend.dto.UserResponse;
import com.socialmedia.socialmedia_backend.entity.User;
import com.socialmedia.socialmedia_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setBio(request.getBio());
        user.setProfilePicUrl(request.getProfilePicUrl());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        return mapToResponse(user);
    }

    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setBio(request.getBio());
        user.setProfilePicUrl(request.getProfilePicUrl());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserResponse> responses = new ArrayList<>();

        for (User user : users) {
            responses.add(mapToResponse(user));
        }

        return responses;
    }


    public List<UserResponse> searchUsers(String username) {

        List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);

        List<UserResponse> responses = new ArrayList<>();

        for (User user : users) {
            responses.add(mapToResponse(user));
        }

        return responses;
    }

    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    public UserResponse toggleBan(Long targetUserId, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new EntityNotFoundException("Admin user not found"));

        if (!"ADMIN".equals(admin.getRole()) && !"luvnichat06".equals(admin.getUsername())) {
            throw new IllegalArgumentException("Access denied: Only administrators can perform this action");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + targetUserId));

        if ("luvnichat06".equals(targetUser.getUsername())) {
            throw new IllegalArgumentException("Cannot ban the primary administrator account");
        }

        targetUser.setBanned(!targetUser.isBanned());
        User updated = userRepository.save(targetUser);
        return mapToResponse(updated);
    }

    private UserResponse mapToResponse(User user) {

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setBio(user.getBio());
        response.setProfilePicUrl(user.getProfilePicUrl());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole() != null ? user.getRole() : "USER");
        response.setBanned(user.isBanned());
        response.setVerified(user.isVerified());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

}
