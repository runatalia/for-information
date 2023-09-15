package com.example.userservice.service;

import com.example.userservice.dto.request.SkillRequestDto;
import com.example.userservice.entity.Candidate;
import com.example.userservice.entity.CandidateSkill;

import java.util.Collection;
import java.util.Set;

public interface CandidateSkillService {
    Set<CandidateSkill> createOrUpdateCascade(Collection<SkillRequestDto> skillRequestDtos, Candidate candidate);
}
