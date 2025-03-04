package com.munchies_backend.spring_boot.repository;

import com.munchies_backend.spring_boot.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRespository extends JpaRepository<Restaurant, Long> {

}
