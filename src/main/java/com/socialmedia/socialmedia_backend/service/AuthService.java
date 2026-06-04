package com.socialmedia.socialmedia_backend.service;

import com.socialmedia.socialmedia_backend.dto.LoginRequest;
import com.socialmedia.socialmedia_backend.dto.LoginResponse;
import com.socialmedia.socialmedia_backend.dto.UserRequest;
import com.socialmedia.socialmedia_backend.dto.UserResponse;
import com.socialmedia.socialmedia_backend.entity.User;
import com.socialmedia.socialmedia_backend.exception.ResourceNotFoundException;
import com.socialmedia.socialmedia_backend.repository.UserRepository;
import com.socialmedia.socialmedia_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponse signup(UserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // hash it
        user.setBio(request.getBio());
        user.setProfilePicUrl(request.getProfilePicUrl());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // Assign ADMIN role to primary moderator account luvnichat06
        if ("luvnichat06".equalsIgnoreCase(request.getUsername())) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }
        user.setBanned(false);

        User saved = userRepository.save(user); //did this as when we do this object creation the using this id and createdAt is assiged

        UserResponse response = new UserResponse();
        response.setId(saved.getId());
        response.setUsername(saved.getUsername());
        response.setEmail(saved.getEmail());
        response.setBio(saved.getBio());
        response.setProfilePicUrl(saved.getProfilePicUrl());
        response.setFirstName(saved.getFirstName());
        response.setLastName(saved.getLastName());
        response.setRole(saved.getRole());
        response.setBanned(saved.isBanned());
        response.setVerified(saved.isVerified());
        response.setCreatedAt(saved.getCreatedAt());

        return response;
    }

    public LoginResponse login(LoginRequest request) {

        // find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email"));

        // check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        // block login if banned
        if (user.isBanned()) {
            throw new IllegalArgumentException("Your account has been banned by an administrator.");
        }

        // Self-healing: if the logging in user is luvnichat06, ensure they are ADMIN
        if ("luvnichat06".equalsIgnoreCase(user.getUsername()) && !"ADMIN".equals(user.getRole())) {
            user.setRole("ADMIN");
            user = userRepository.save(user);
        }

        // generate token
        String token = jwtUtil.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setUserId(user.getId());

        return response;
    }
}