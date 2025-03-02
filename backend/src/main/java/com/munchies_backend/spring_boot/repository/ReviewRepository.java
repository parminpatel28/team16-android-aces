package com.munchies_backend.spring_boot.repository;

import com.munchies_backend.spring_boot.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {


}
