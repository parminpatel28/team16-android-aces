package com.munchies_backend.spring_boot.services;

import com.munchies_backend.spring_boot.repository.RestaurantRespository;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    private final RestaurantRespository restaurantRespository;

    public RestaurantService(RestaurantRespository restaurantRespository) {
        this.restaurantRespository = restaurantRespository;
    }
}
