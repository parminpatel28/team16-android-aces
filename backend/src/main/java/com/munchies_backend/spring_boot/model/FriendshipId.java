package com.munchies_backend.spring_boot.model;

import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class FriendshipId implements java.io.Serializable {
    private static final long serialVersionUID = -8401988947802857157L;
    private Integer user;
    private Integer friend;

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getFriend() {
        return friend;
    }

    public void setFriend(Integer friend) {
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