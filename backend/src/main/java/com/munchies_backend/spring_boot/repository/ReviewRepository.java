package com.munchies_backend.spring_boot.repository;

import com.munchies_backend.spring_boot.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByUserId(Integer userId); // Fetch reviews by user ID
}
