package com.munchies_backend.spring_boot.repository;

import com.munchies_backend.spring_boot.model.FriendRequestStatus;
import com.munchies_backend.spring_boot.model.Friendship;
import com.munchies_backend.spring_boot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository  extends JpaRepository<Friendship, Long> {

    List<Friendship> findByUserId(User user);
    List<Friendship> findByFriendId(User friend);

    Optional<Friendship> findByUserAndFriend(User user, User friend);

    List<Friendship> findByUserAndStatus(User user, FriendRequestStatus status);
    List<Friendship> findByFriendAndStatus(User friend, FriendRequestStatus status);

    List<Friendship> findByUserIdAndStatus(Integer userId, FriendRequestStatus status);
    List<Friendship> findByFriendIdAndStatus(Integer friendId, FriendRequestStatus status);
}
