package com.example.userservice.mapper;

import com.example.userservice.dto.response.SkillResponseDto;
import com.example.userservice.entity.CandidateSkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateSkillMapper {

    @Mapping(target = "id", expression = "java(candidateSkill.getSkill() != null ?  candidateSkill.getSkill().getId() : null)")
    @Mapping(target = "name", expression = "java(candidateSkill.getSkill() != null ? candidateSkill.getSkill().getName() : candidateSkill.getCustomSkill())")
    SkillResponseDto toDTO(CandidateSkill candidateSkill);
}