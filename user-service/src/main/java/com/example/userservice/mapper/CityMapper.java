package com.example.userservice.mapper;

import com.example.userservice.dto.request.CityRequestDto;
import com.example.userservice.dto.response.CityResponseDto;
import com.example.userservice.entity.City;
import com.example.userservice.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface CityMapper {

    City fromDTO(CityRequestDto cityRequestDto);

    CityResponseDto toDTO(City city);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    void update(@MappingTarget City city, Country country);
}
