package com.munchies_backend.spring_boot.repository;

import com.munchies_backend.spring_boot.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.user.id = :userId ORDER BY r.date DESC")
    List<Review> findByUserIdOrderByDateDesc(@Param("userId") String userId);

    @Query("SELECT r FROM Review r WHERE r.restaurantId = :restaurantId")
    List<Review> findReviewsByRestaurantId(@Param("restaurantId") String restaurantId);

}
