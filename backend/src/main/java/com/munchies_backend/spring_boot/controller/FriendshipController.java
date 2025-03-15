package com.munchies_backend.spring_boot.controller;

import com.munchies_backend.spring_boot.model.User;
import com.munchies_backend.spring_boot.model.Friendship;
import com.munchies_backend.spring_boot.services.UserService;
import com.munchies_backend.spring_boot.services.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    // Send a friend request
    @PostMapping("/{senderId}/{receiverId}")
    public ResponseEntity<Friendship> createFriendship(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
        Friendship savedFriendship = friendshipService.saveFriendship(senderId, receiverId);
        return ResponseEntity.ok(savedFriendship);
    }

    // Delete a friendship/cancel friend request
    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable Integer userId, @PathVariable Integer friendId) {
        // TODO STILL
        friendshipService.deleteFriendship(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    // Accept a friend request
    @PostMapping("/{senderId}/{receiverId}/accept")
    public ResponseEntity<Friendship> acceptFriendship(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
        friendshipService.acceptFriendship(senderId, receiverId);
        return ResponseEntity.noContent().build();
    }

    // Reject a friend request
    @PostMapping("/{senderId}/{receiverId}/reject")
    public ResponseEntity<Friendship> rejectFriendship(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
        friendshipService.rejectFriendship(senderId, receiverId);
        return ResponseEntity.noContent().build();
    }

    // Get all of a user's friendships
    @GetMapping("/{userId}")
    public ResponseEntity<List<User>> getAcceptedFriends(@PathVariable Integer userId) {
        List<User> friendships = friendshipService.getAcceptedFriends(userId);
        return ResponseEntity.ok(friendships);
    }
}
