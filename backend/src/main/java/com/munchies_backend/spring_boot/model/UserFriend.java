package com.munchies_backend.spring_boot.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_friends", schema = "public")
public class UserFriend {
    @SequenceGenerator(name = "user_friends_id_gen", sequenceName = "reviews_review_id_seq", allocationSize = 1)
    @EmbeddedId
    private UserFriendId id;

    public UserFriendId getId() {
        return id;
    }

    public void setId(UserFriendId id) {
        this.id = id;
    }

}