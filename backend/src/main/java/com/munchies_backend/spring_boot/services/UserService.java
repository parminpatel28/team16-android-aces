package com.munchies_backend.spring_boot.services;

import com.munchies_backend.spring_boot.model.User;
import com.munchies_backend.spring_boot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Save a user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get a user by ID
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }


    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a user by ID
    public void deleteUser(Integer id) {
        userRepository.deleteById(Long.valueOf(id));
    }

    public Optional<Map<Integer, Object>> getFriends(long userId) {
        return Optional.ofNullable(userRepository.findById(userId)
                .map(User::getFriends)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public User addFriend(long userId, int friendKey, Object friendData) {
        return userRepository.findById(userId)
                .map(user -> {
                    // Retrieve the current friends map, or create a new one if it's null
                    Map<Integer, Object> friends = user.getFriends();
                    if (friends == null) {
                        friends = new HashMap<>();
                    }
                    // Add the new friend to the map
                    friends.put(friendKey, friendData);
                    // Update the user with the new friends map
                    user.setFriends(friends);
                    // Save the updated user back to the database
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User deleteFriend(long userId, int friendKey) {
        return userRepository.findById(userId)
                .map(user -> {
                    // Retrieve the current friends map, or create a new one if it's null
                    Map<Integer, Object> friends = user.getFriends();
                    if (friends == null) {
                        friends = new HashMap<>();
                    }
                    // Add the new friend to the map
                    friends.remove(friendKey);
                    // Update the user with the new friends map
                    user.setFriends(friends);
                    // Save the updated user back to the database
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
