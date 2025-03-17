package com.munchies_backend.spring_boot.model;

import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class FriendshipId implements java.io.Serializable {
    private static final long serialVersionUID = -8401988947802857157L;
    private String user;
    private String friend;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FriendshipId entity = (FriendshipId) o;
        return Objects.equals(this.friend, entity.friend) &&
                Objects.equals(this.user, entity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friend, user);
    }

}