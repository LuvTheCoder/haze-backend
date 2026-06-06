package com.socialmedia.socialmedia_backend.service;


import com.socialmedia.socialmedia_backend.entity.User;
import com.socialmedia.socialmedia_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;

    public String toggleFollow(Long currentUserId, Long targerUserId){
        if(currentUserId.equals(targerUserId)){
            throw new RuntimeException("You cannot follow yourself");
        }

        User currentUser = userRepository.findById(currentUserId).orElseThrow(()-> new RuntimeException("Current user not found!"));
        User targetUser = userRepository.findById(targerUserId).orElseThrow(()->new RuntimeException("Target user not found!"));

        boolean isFollowing = userRepository.isAlreadyFollowing(currentUserId, targerUserId);

        if(isFollowing){
            targetUser.getFollowers().remove(currentUser);
            userRepository.save(targetUser);
            return "Unfollowed successfully";
        }
        else{
            //Follow
            targetUser.getFollowers().add(currentUser);
            userRepository.save(targetUser);
            return "Followed successfully";
        }
    }

    public long getFollowersCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFollowers().size();
    }

    public long getFollowingCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFollowing().size();
    }

    public boolean isFollowing(Long currentUserId, Long targetUserId) {
        return userRepository.isAlreadyFollowing(currentUserId, targetUserId);
    }
}
