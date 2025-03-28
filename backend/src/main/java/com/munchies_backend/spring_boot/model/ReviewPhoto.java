package com.munchies_backend.spring_boot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "review_photos")
public class ReviewPhoto {
    @Id
    @Column(name = "review_id", nullable = false)
    private Integer reviewId;

    @Column(name = "photo_url", length = Integer.MAX_VALUE)
    private String photoUrl;

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}