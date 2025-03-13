package com.munchies_backend.spring_boot.services;

import com.munchies_backend.spring_boot.model.Friendship;
import com.munchies_backend.spring_boot.repository.FriendshipRepository;
import com.munchies_backend.spring_boot.model.FriendRequestStatus;
import com.munchies_backend.spring_boot.model.User;
import com.munchies_backend.spring_boot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private UserRepository userRepository;


    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    // Get all friendships
    public List<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    // Save a friendship
    public Friendship saveFriendship(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Friendship friendship = new Friendship();
        friendship.setUser(sender);
        friendship.setFriend(receiver);
        friendship.setStatus(FriendRequestStatus.PENDING);
        friendship.setUpdatedAt(LocalDateTime.now());
        return friendshipRepository.save(friendship);
    }

    // Get a friendship by user and friend
    public Optional<Friendship> findFriendshipBySenderAndUser(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));
        Optional<Friendship> first =  friendshipRepository.findByUserAndFriend(friend, user);
        Optional<Friendship> second =  friendshipRepository.findByUserAndFriend(user, friend);
        if (first.isPresent()) {
            return first;
        } else {
            return second;
        }
    }

    // Get all of a user's Friends
    public List<User> getAcceptedFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Friendship> sentRequests = friendshipRepository.findByUserAndStatus(user, FriendRequestStatus.ACCEPTED);
        List<Friendship> receivedRequests = friendshipRepository.findByFriendAndStatus(user, FriendRequestStatus.ACCEPTED);

        List<User> friendsFromSent = sentRequests.stream()
                .map(Friendship::getFriend)
                .toList();

        List<User> friendsFromReceived = receivedRequests.stream()
                .map(Friendship::getUser)
                .toList();

        return Stream.concat(friendsFromSent.stream(), friendsFromReceived.stream())
                .collect(Collectors.toList());
    }

    // Get all list of all Users who have sent incoming requests to this user
    public List<User> getIncomingRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Friendship> receivedRequests = friendshipRepository.findByFriendAndStatus(user, FriendRequestStatus.PENDING);
        List<User> usersFromReceived = receivedRequests.stream()
                .map(Friendship::getUser)
                .toList();
        return usersFromReceived;
    }

    // Get all list of all Users who have been sent outgoing requests by this user
    public List<User> getOutgoingRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Friendship> sentRequests = friendshipRepository.findByUserAndStatus(user, FriendRequestStatus.PENDING);
        List<User> usersFromSent = sentRequests.stream()
                .map(Friendship::getFriend)
                .toList();
        return usersFromSent;
    }

    public void acceptFriendship(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));
        Friendship friendship = friendshipRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        // Update status
        friendship.accept();
        friendshipRepository.save(friendship);

        // Optionally, update both users' friend lists
        User sender = friendship.getUser();
        User receiver = friendship.getFriend();
        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public void rejectFriendship(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));
        Friendship friendship = friendshipRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        // Update status
        friendship.reject();
        friendshipRepository.save(friendship);

        // Optionally, update both users' friend lists
        User sender = friendship.getUser();
        User receiver = friendship.getFriend();
        sender.getFriends().remove(receiver);
        receiver.getFriends().remove(sender);
        userRepository.save(sender);
        userRepository.save(receiver);
    }


}
