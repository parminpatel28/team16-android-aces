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
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "profile_picture", length = Integer.MAX_VALUE)
    private String profilePicture;

    @Column(name = "user_bio", length = Integer.MAX_VALUE)
    private String userBio;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "account_creation_date")
    private Instant accountCreationDate;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "location_id")
    private Location location;

    @ColumnDefault("'[]'::jsonb")
    @Column(name = "friends")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> friends;

    @ColumnDefault("'[]'::jsonb")
    @Column(name = "saved_reviews")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> savedReviews;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public Instant getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Instant accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Map<String, Object> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, Object> friends) {
        this.friends = friends;
    }

    public Map<String, Object> getSavedReviews() {
        return savedReviews;
    }

    public void setSavedReviews(Map<String, Object> savedReviews) {
        this.savedReviews = savedReviews;
    }

}