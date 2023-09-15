package com.example.userservice.mapper;

import com.example.userservice.dto.request.CandidateRequestDto;
import com.example.userservice.dto.request.UserEmailDto;
import com.example.userservice.dto.request.UserNameDto;
import com.example.userservice.dto.response.FullCandidateInfoResponseDto;
import com.example.userservice.dto.response.MainCandidateInfoResponseDto;
import com.example.userservice.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;


@Mapper(componentModel = "spring", uses = {EducationMapper.class, CityMapper.class,
        CandidateSkillMapper.class, SpecialtyMapper.class, ExperienceMapper.class})
public interface CandidateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "educations", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "isBlocked", ignore = true)
    Candidate fromDTO(CandidateRequestDto candidateRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    @Mapping(target = "educations", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "himselfDescription", ignore = true)
    @Mapping(target = "isBlocked", ignore = true)
    void updateName(@MappingTarget Candidate candidate, UserNameDto userNameDto);

    @Mapping(target = "email", source = "userEmailDto.newEmail")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    @Mapping(target = "educations", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "himselfDescription", ignore = true)
    @Mapping(target = "isBlocked", ignore = true)
    void updateEmail(@MappingTarget Candidate candidate, UserEmailDto userEmailDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "himselfDescription", ignore = true)
    @Mapping(target = "isBlocked", ignore = true)
    void update(@MappingTarget Candidate candidate, City city, Set<Education> educations,
                Set<CandidateSkill> skills, Set<Specialty> specialties, Set<Experience> experiences);

    FullCandidateInfoResponseDto toFullDTO(Candidate candidate);

    MainCandidateInfoResponseDto toMainDTO(Candidate candidate);

}
