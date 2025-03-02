package com.munchies_backend.spring_boot.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "reviews", schema = "public")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_gen")
    @SequenceGenerator(name = "reviews_id_gen", sequenceName = "reviews_review_id_seq", allocationSize = 1)
    @Column(name = "review_id", nullable = false)
    private Integer id;

    @Column(name = "caption", length = Integer.MAX_VALUE)
    private String caption;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date")
    private Instant date;

    @Column(name = "rating")
    private Double rating;

    @ColumnDefault("0")
    @Column(name = "likes")
    private Integer likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "location_id")
    private Location location;

    @ColumnDefault("'[]'::jsonb")
    @Column(name = "tagged_users")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> taggedUsers;

    @ColumnDefault("'[]'::jsonb")
    @Column(name = "restaurants")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> restaurants;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Map<String, Object> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(Map<String, Object> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public Map<String, Object> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(Map<String, Object> restaurants) {
        this.restaurants = restaurants;
    }

}