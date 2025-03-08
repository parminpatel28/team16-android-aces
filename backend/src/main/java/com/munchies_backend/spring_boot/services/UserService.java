package com.munchies_backend.spring_boot.services;

import com.munchies_backend.spring_boot.model.User;
import com.munchies_backend.spring_boot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create user method
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
