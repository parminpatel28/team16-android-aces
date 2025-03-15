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

    // Get a user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a user by ID
    public void deleteUser(Integer id) {
        userRepository.deleteById(Long.valueOf(id));
    }
}
