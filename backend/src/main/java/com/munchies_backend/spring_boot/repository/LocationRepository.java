package com.munchies_backend.spring_boot.repository;

import com.munchies_backend.spring_boot.model.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findAllById(int id);

}
