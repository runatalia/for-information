package com.example.userservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateRequestDto {
    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @Past
    @NotNull
    private LocalDate birthdate;

    @NotNull
    private CityRequestDto city;

    @NotNull
    private Set<EducationRequestDto> educations;


    @NotNull
    private Set<ExperienceRequestDto> experiences;

    @NotNull
    @NotEmpty
    private List<Short> specialityIds;

    @NotNull
    private Set<SkillRequestDto> skills;

    private String himselfDescription;
}