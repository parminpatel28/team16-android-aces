package com.munchies_backend.spring_boot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class ReviewTaggedUserId implements java.io.Serializable {
    private static final long serialVersionUID = 6606009940085628234L;
    @Column(name = "review_id", nullable = false)
    private Integer reviewId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReviewTaggedUserId entity = (ReviewTaggedUserId) o;
        return Objects.equals(this.reviewId, entity.reviewId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, userId);
    }

}