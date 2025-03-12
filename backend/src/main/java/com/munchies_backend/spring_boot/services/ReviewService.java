package com.munchies_backend.spring_boot.services;

import com.munchies_backend.spring_boot.model.Review;
import com.munchies_backend.spring_boot.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Save a review
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    // Get a review by ID
    public Optional<Review> getReviewById(Integer id) {
        return reviewRepository.findById(id);
    }

    // Get all reviews
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Get reviews by user ID
    public List<Review> getReviewsByUserId(Integer userId) {
        return reviewRepository.findByUserIdOrderByDateDesc(userId);
    }

    // Delete a review by ID
    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }
}
