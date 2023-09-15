package com.example.userservice.service.impl;

import com.example.userservice.dto.request.SkillRequestDto;
import com.example.userservice.entity.Candidate;
import com.example.userservice.entity.CandidateSkill;
import com.example.userservice.entity.Skill;
import com.example.userservice.repository.CandidateSkillRepository;
import com.example.userservice.service.CandidateSkillService;
import com.example.userservice.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateSkillServiceImpl implements CandidateSkillService {
    private static final String SKILL_NOT_SPECIFIED = "Skill not specified.";
    private final SkillService skillService;
    private final CandidateSkillRepository candidateSkillRepository;

    @Transactional
    @Override
    public Set<CandidateSkill> createOrUpdateCascade(Collection<SkillRequestDto> skillRequestDtos, Candidate candidate) {
        return skillRequestDtos.stream().map(skillRequestDto -> {
            var skillId = skillRequestDto.getId();
            var skillName = skillRequestDto.getName();

            if (skillId == null && skillName == null)
                throw new IllegalArgumentException(SKILL_NOT_SPECIFIED);

            if (skillId != null) {
                var skill = skillService.findById(skillRequestDto.getId());
                return createOrUpdateBySkill(candidate, skill);
            } else {
                var skillOptional = skillService.findByName(skillRequestDto.getName());
                return skillOptional.isPresent()
                        ? createOrUpdateBySkill(candidate, skillOptional.get())
                        : createOrUpdateByCustomSkill(candidate, skillName);
            }
        }).collect(Collectors.toSet());
    }

    private CandidateSkill createOrUpdateBySkill(Candidate candidate, Skill skill) {
        return candidateSkillRepository.findByCandidateAndSkill(candidate, skill)
                .orElseGet(() -> candidateSkillRepository.save(
                        CandidateSkill.builder()
                                .candidate(candidate)
                                .skill(skill)
                                .build()
                ));
    }

    private CandidateSkill createOrUpdateByCustomSkill(Candidate candidate, String customSkill) {
        return candidateSkillRepository.findByCandidateAndCustomSkill(candidate, customSkill)
                .orElseGet(() -> candidateSkillRepository.save(
                        CandidateSkill.builder()
                                .candidate(candidate)
                                .customSkill(customSkill)
                                .build()
                ));
    }

}