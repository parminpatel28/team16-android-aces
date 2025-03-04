package com.munchies_backend.spring_boot.services;

import com.munchies_backend.spring_boot.model.Location;
import com.munchies_backend.spring_boot.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
