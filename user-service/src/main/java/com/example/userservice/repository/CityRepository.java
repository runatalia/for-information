package com.example.userservice.repository;

import com.example.userservice.entity.City;
import com.example.userservice.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Short> {
    Optional<City> findByNameIgnoreCaseAndCountry(String name, Country country);
}