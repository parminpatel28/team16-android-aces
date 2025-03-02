package com.munchies_backend.spring_boot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class ReviewRestaurantId implements java.io.Serializable {
    private static final long serialVersionUID = 6333942121901454903L;
    @Column(name = "review_id", nullable = false)
    private Integer reviewId;

    @Column(name = "restaurant_id", nullable = false)
    private Integer restaurantId;

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReviewRestaurantId entity = (ReviewRestaurantId) o;
        return Objects.equals(this.restaurantId, entity.restaurantId) &&
                Objects.equals(this.reviewId, entity.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, reviewId);
    }

}