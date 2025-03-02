package com.munchies_backend.spring_boot.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "review_restaurants", schema = "public")
public class ReviewRestaurant {
    @SequenceGenerator(name = "review_restaurants_id_gen", sequenceName = "restaurants_restaurant_id_seq", allocationSize = 1)
    @EmbeddedId
    private ReviewRestaurantId id;

    @MapsId("restaurantId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public ReviewRestaurantId getId() {
        return id;
    }

    public void setId(ReviewRestaurantId id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}