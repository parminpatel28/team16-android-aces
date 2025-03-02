package com.munchies_backend.spring_boot.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "review_tagged_users", schema = "public")
public class ReviewTaggedUser {
    @SequenceGenerator(name = "review_tagged_users_id_gen", sequenceName = "restaurants_restaurant_id_seq", allocationSize = 1)
    @EmbeddedId
    private ReviewTaggedUserId id;

    public ReviewTaggedUserId getId() {
        return id;
    }

    public void setId(ReviewTaggedUserId id) {
        this.id = id;
    }

}