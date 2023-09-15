package com.example.userservice.service.impl;

import com.example.userservice.entity.City;
import com.example.userservice.mapper.CityMapper;
import com.example.userservice.repository.CityRepository;
import com.example.userservice.service.CityService;
import com.example.userservice.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private static final String CITY_NOT_FOUND_BY_ID = "City not found by id %d.";
    private final CityRepository cityRepository;
    private final CountryService countryService;
    private final CityMapper cityMapper;

    @Override
    @Transactional
    public City createOrUpdateCity(City city) {
        var id = city.getId();
        var country = countryService.createOrUpdateCountry(city.getCountry());
        if (id == null)
            return cityRepository.findByNameIgnoreCaseAndCountry(city.getName(), country)
                    .orElseGet(() -> {
                        cityMapper.update(city, country);
                        return cityRepository.save(city);
                    });
        else
            return cityRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(CITY_NOT_FOUND_BY_ID.formatted(id)));

    }
}