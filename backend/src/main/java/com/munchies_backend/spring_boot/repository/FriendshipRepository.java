package com.munchies_backend.spring_boot.repository;

import com.munchies_backend.spring_boot.model.FriendRequestStatus;
import com.munchies_backend.spring_boot.model.Friendship;
import com.munchies_backend.spring_boot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUser(User user);
    List<Friendship> findByFriend(User friend);
    List<Friendship> findByUserId(String user);
    List<Friendship> findByFriendId(String friend);

    Optional<Friendship> findByUserAndFriend(User user, User friend);
    Optional<Friendship> findByUserIdAndFriendId(String userId, String friendId);

    List<Friendship> findByUserAndStatus(User user, FriendRequestStatus status);
    List<Friendship> findByFriendAndStatus(User friend, FriendRequestStatus status);

    List<Friendship> findByUserIdAndStatus(String userId, FriendRequestStatus status);
    List<Friendship> findByFriendIdAndStatus(String friendId, FriendRequestStatus status);
}
