package com.example.userservice.repository;

import com.example.userservice.entity.Candidate;
import com.example.userservice.entity.CandidateSkill;
import com.example.userservice.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, Long> {
    Optional<CandidateSkill> findByCandidateAndSkill(Candidate candidate, Skill skill);

    Optional<CandidateSkill> findByCandidateAndCustomSkill(Candidate candidate, String customSkill);
}