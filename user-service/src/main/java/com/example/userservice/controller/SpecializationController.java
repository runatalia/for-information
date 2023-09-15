package com.example.userservice.controller;

import com.example.userservice.annotation.swagger.SwaggerGetAllByName;
import com.example.userservice.dto.response.SpecializationResponseDto;
import com.example.userservice.service.SpecializationService;
import com.example.userservice.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@Tag(name = "Specialization Controller.", description = "Work with specializations.")
@RequestMapping("/specializations")
public class SpecializationController {
    private final SpecializationService specializationService;

    @Operation(summary = "Get all specializations by name.", description = "Return all specializations from postgreSQL by name.")
    @SwaggerGetAllByName
    @GetMapping(params = {"name"})
    public ResponseEntity<List<SpecializationResponseDto>> findAllByName(@RequestParam @NotBlank String name) {
        var specializationDtoList = specializationService.findAllByName(name);
        return ResponseBuilder.build(specializationDtoList);
    }

    @Operation(summary = "Get all specializations by name.", description = "Return all specializations from postgreSQL" +
            " by name pagination support.")
    @SwaggerGetAllByName
    @GetMapping(params = {"name", "page", "size"})
    public ResponseEntity<List<SpecializationResponseDto>> findAllByName(@RequestParam @NotBlank String name,
                                                                         @RequestParam int page,
                                                                         @RequestParam int size) {
        var specializationDtoPage = specializationService.findAllByName(name, PageRequest.of(page, size));
        return ResponseBuilder.build(specializationDtoPage);
    }
}
