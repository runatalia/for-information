package com.example.userservice.controller;

import com.example.userservice.annotation.swagger.SwaggerGetAllByName;
import com.example.userservice.dto.response.SpecialtyResponseDto;
import com.example.userservice.service.SpecialtyService;
import com.example.userservice.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Specialty Controller.", description = "Work with specialties.")
@RequestMapping("/specialties")
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    @Operation(summary = "Get all specialties by name.", description = "Return all specialties from postgreSQL by name.")
    @SwaggerGetAllByName
    @GetMapping
    public ResponseEntity<List<SpecialtyResponseDto>> findAll() {
        var specialtyDtoList = specialtyService.findAll();
        return ResponseBuilder.build(specialtyDtoList);
    }

    @Operation(summary = "Get all specialties by name.", description = "Return all specialties from postgreSQL" +
            " by name pagination support.")
    @SwaggerGetAllByName
    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<SpecialtyResponseDto>> findAll(@RequestParam int page,
                                                              @RequestParam int size) {
        var specialtyDtoPage = specialtyService.findAll(PageRequest.of(page, size));
        return ResponseBuilder.build(specialtyDtoPage);
    }
}
