package com.munchies_backend.spring_boot.controller;

import com.munchies_backend.spring_boot.model.User;
import com.munchies_backend.spring_boot.model.Friendship;
import com.munchies_backend.spring_boot.services.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    // Send a friend request
    @PostMapping("/{senderId}/{receiverId}")
    public ResponseEntity<Void> createFriendship(@PathVariable String senderId, @PathVariable String receiverId) {
        Friendship savedFriendship = friendshipService.saveFriendship(senderId, receiverId);
        return ResponseEntity.noContent().build();
    }

    // Delete a friendship/cancel friend request
    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable String userId, @PathVariable String friendId) {
        friendshipService.deleteFriendship(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    // Accept a friend request
    @PostMapping("/{senderId}/{receiverId}/accept")
    public ResponseEntity<Friendship> acceptFriendship(@PathVariable String senderId, @PathVariable String receiverId) {
        friendshipService.acceptFriendship(senderId, receiverId);
        return ResponseEntity.noContent().build();
    }

    // Reject a friend request (unused)
    @PostMapping("/{senderId}/{receiverId}/reject")
    public ResponseEntity<Friendship> rejectFriendship(@PathVariable String senderId, @PathVariable String receiverId) {
        friendshipService.rejectFriendship(senderId, receiverId);
        return ResponseEntity.noContent().build();
    }

    // Get all of a user's friendships
    @GetMapping("/{userId}")
    public ResponseEntity<List<User>> getAcceptedFriends(@PathVariable String userId) {
        List<User> friendships = friendshipService.getAcceptedFriends(userId);
        return ResponseEntity.ok(friendships);
    }

    // Get all of a user's incoming friend requests
    @GetMapping("/{userId}/incoming")
    public ResponseEntity<List<User>> getIncomingRequests(@PathVariable String userId) {
        List<User> friendships = friendshipService.getIncomingRequests(userId);
        return ResponseEntity.ok(friendships);
    }

    // Get all of a user's outgoing friend requests
    @GetMapping("/{userId}/outgoing")
    public ResponseEntity<List<User>> getOutgoingRequests(@PathVariable String userId) {
        List<User> friendships = friendshipService.getOutgoingRequests(userId);
        return ResponseEntity.ok(friendships);
    }
}
