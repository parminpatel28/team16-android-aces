package com.munchies_backend.spring_boot.repository;

import com.munchies_backend.spring_boot.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByUserIdOrderByDateDesc(String userId); // Fetch reviews by user ID
}
