package com.munchies_backend.spring_boot.controller;

import com.munchies_backend.spring_boot.model.User;
import com.munchies_backend.spring_boot.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Get friends
    @GetMapping("/{id}/friend")
    public ResponseEntity<Map<Integer, Object>> getFriends(@PathVariable Integer id) {
        Optional<Map<Integer, Object>> user = userService.getFriends(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/{id}/friend/{friendId}")
    public ResponseEntity<User> createUserFriend(@PathVariable Integer id, @PathVariable Integer friendId, @RequestBody Object friendData) {
        User UserFriend = userService.addFriend(id, friendId, friendData);
        return ResponseEntity.ok(UserFriend);
    }
    @DeleteMapping("/{id}/friend/{friendId}")
    public ResponseEntity<User> deleteUserFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        User UserFriend = userService.deleteFriend(id, friendId);
        return ResponseEntity.ok(UserFriend);
    }
}
