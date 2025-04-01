package com.munchies_backend.spring_boot.services;

import com.munchies_backend.spring_boot.model.Friendship;
import com.munchies_backend.spring_boot.repository.FriendshipRepository;
import com.munchies_backend.spring_boot.model.FriendRequestStatus;
import com.munchies_backend.spring_boot.model.User;
import com.munchies_backend.spring_boot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;


    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    // Get all friendships
    public List<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    // Save a friendship
    public Friendship saveFriendship(String senderId, String receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Optional<Friendship> existing1 = friendshipRepository.findByUserIdAndFriendId(senderId, receiverId);
        Optional<Friendship> existing2 = friendshipRepository.findByUserIdAndFriendId(receiverId, senderId);

        if (!(existing1.isPresent() || existing2.isPresent())) {
            Friendship friendship = new Friendship();
            friendship.setUser(sender);
            friendship.setFriend(receiver);
            friendship.setStatus(FriendRequestStatus.PENDING);
            friendship.setUpdatedAt(LocalDateTime.now());
            return friendshipRepository.save(friendship);
        } else if (existing1.isPresent()) {
            return existing1.get();
        }
        return existing2.get();
    }

    // Get a friendship by user and friend
    public Optional<Friendship> findFriendshipBySenderAndUser(String userId, String friendId) {
        Optional<Friendship> first = friendshipRepository.findByUserIdAndFriendId(friendId, userId);
        Optional<Friendship> second = friendshipRepository.findByUserIdAndFriendId(userId, friendId);
        if (first.isPresent()) {
            return first;
        } else {
            return second;
        }
    }

    // Get all of a user's Friends
    public List<User> getAcceptedFriends(String userId) {
        List<Friendship> sentRequests = friendshipRepository.findByUserIdAndStatus(userId, FriendRequestStatus.ACCEPTED);
        List<Friendship> receivedRequests = friendshipRepository.findByFriendIdAndStatus(userId, FriendRequestStatus.ACCEPTED);

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
    public List<User> getIncomingRequests(String userId) {
        List<Friendship> receivedRequests = friendshipRepository.findByFriendIdAndStatus(userId, FriendRequestStatus.PENDING);
        List<User> usersFromReceived = receivedRequests.stream()
                .map(Friendship::getUser)
                .toList();
        return usersFromReceived;
    }

    // Get all list of all Users who have been sent outgoing requests by this user
    public List<User> getOutgoingRequests(String userId) {
        List<Friendship> sentRequests = friendshipRepository.findByUserIdAndStatus(userId, FriendRequestStatus.PENDING);
        List<User> usersFromSent = sentRequests.stream()
                .map(Friendship::getFriend)
                .toList();
        return usersFromSent;
    }

    public void acceptFriendship(String userId, String friendId) {
        Friendship friendship;
        Optional<Friendship> first = friendshipRepository.findByUserIdAndFriendId(friendId, userId);
        Optional<Friendship> second = friendshipRepository.findByUserIdAndFriendId(userId, friendId);
        if (first.isPresent()) {
            friendship = first.get();
        } else if (second.isPresent()) {
            friendship = second.get();
        } else {
            throw new RuntimeException("Friendship not found");
        }

        // Update status
        friendship.accept();
        friendship.setUpdatedAt(LocalDateTime.now());
        friendshipRepository.save(friendship);

        // Optionally, update both users' friend lists
//        User sender = friendship.getUser();
//        User receiver = friendship.getFriend();
//        sender.getFriends().add(receiver);
//        receiver.getFriends().add(sender);
//        userRepository.save(sender);
//        userRepository.save(receiver);
    }

    public void rejectFriendship(String userId, String friendId) {
        Friendship friendship = friendshipRepository.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        // Update status
        friendship.reject();
        friendship.setUpdatedAt(LocalDateTime.now());
        friendshipRepository.save(friendship);

        // Optionally, update both users' friend lists
//        User sender = friendship.getUser();
//        User receiver = friendship.getFriend();
//        sender.getFriends().remove(receiver);
//        receiver.getFriends().remove(sender);
//        userRepository.save(sender);
//        userRepository.save(receiver);
    }

    public void deleteFriendship(String userId, String friendId) {
        Friendship friendship;
        Optional<Friendship> first = friendshipRepository.findByUserIdAndFriendId(friendId, userId);
        Optional<Friendship> second = friendshipRepository.findByUserIdAndFriendId(userId, friendId);
        if (first.isPresent()) {
            friendship = first.get();
        } else if (second.isPresent()) {
            friendship = second.get();
        } else {
            throw new RuntimeException("Friendship not found");
        }
        friendshipRepository.delete(friendship);
    }
}
