package com.munchies_backend.spring_boot.model;

import jakarta.persistence.*;
import com.munchies_backend.spring_boot.model.FriendRequestStatus;
import com.munchies_backend.spring_boot.model.User;
import com.munchies_backend.spring_boot.model.FriendshipId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "friendships", schema = "public")
@IdClass(FriendshipId.class)
public class Friendship implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Friendship() {
        this.status = FriendRequestStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public FriendRequestStatus getStatus() { return status; }

    public void setStatus(FriendRequestStatus status) { this.status = status; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void accept() {
        this.status = FriendRequestStatus.ACCEPTED;
    }

    public void reject() {
        this.status = FriendRequestStatus.REJECTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(friend, that.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }
}