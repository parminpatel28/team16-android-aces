package com.munchies_backend.spring_boot.services;

import com.munchies_backend.spring_boot.model.CreateUserRequest;
import com.munchies_backend.spring_boot.model.User;
import com.munchies_backend.spring_boot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // Get a user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a user by ID
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User createUser(CreateUserRequest request) {
        // Check if username is already taken
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        User user = new User();
        user.setId(request.getFirebaseUserId());
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setProfilePicture(request.getProfilePicture());
        user.setUserBio(request.getUserBio());
        user.setEmailAddress(request.getEmailAddress());
        user.setAccountCreationDate(Instant.now());

        return userRepository.save(user);
    }
}
