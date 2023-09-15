package com.example.userservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityResponseDto {
    private Short id;
    private String name;
    private CountryResponseDto country;
}