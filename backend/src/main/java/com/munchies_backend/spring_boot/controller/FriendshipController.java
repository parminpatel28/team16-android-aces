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

    // Create a new user
    @PostMapping
    public ResponseEntity<Friendship> createFriendship(@RequestBody Friendship friendship) {
        User savedFriendship = friendshipService.saveFriendship(friendship);
        return ResponseEntity.ok(savedUserfriend);
    }

//    // Get a friendship by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Friendship> getFriendshipById(@PathVariable Integer id) {
//        Optional<Friendship> friendship = friendshipService.getFriendshipById(id);
//        return friendship.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Get all friendships
//    @GetMapping
//    public ResponseEntity<List<Friendship>> getAllFriendships() {
//        List<Friendship> friendships = friendshipService.getAllFriendships();
//        return ResponseEntity.ok(friendships);
//    }
//
//    // Delete a friendship
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteFriendship(@PathVariable Integer id) {
//        friendshipService.deleteFriendship(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//    // Get friends
//    @GetMapping("/{id}")
//    public ResponseEntity<List<Integer>> getFriends(@PathVariable Integer id) {
//        Optional<Map<Integer, Object>> friendship = friendshipService.getFriends(id);
//        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//    @PostMapping("/{id}/friend/{friendId}")
//    public ResponseEntity<User> createFriendship(@PathVariable Integer id, @PathVariable Integer friendId, @RequestBody Object friendData) {
//        User Friendship = userService.addFriend(id, friendId, friendData);
//        return ResponseEntity.ok(Friendship);
//    }
//    @DeleteMapping("/{id}/friend/{friendId}")
//    public ResponseEntity<User> deleteFriendship(@PathVariable Integer id, @PathVariable Integer friendId) {
//        User Friendship = userService.deleteFriend(id, friendId);
//        return ResponseEntity.ok(Friendship);
//    }

    // Get a friendship by ID
    @GetMapping("/{id}")
    public ResponseEntity<Friendship> getFriendshipById(@PathVariable Integer id) {
        Optional<Friendship> friendship = friendshipService.getFriendshipById(id);
        return friendship.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all friendships by userId
    @GetMapping("/{id}")
    public ResponseEntity<List<Friendship>> getAllFriendships() {
        List<Friendship> friendships = friendshipService.getAllFriendships();
        return ResponseEntity.ok(friendships);
    }

    // Delete a friendship
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable Integer id) {
        friendshipService.deleteFriendship(id);
        return ResponseEntity.noContent().build();
    }
}
