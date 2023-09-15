package com.example.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "candidate_skill", schema = "users")
public class CandidateSkill {
    @Id
    @SequenceGenerator(name = "candidate_skill_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidate_skill_sequence")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_candidate", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "id_skill")
    private Skill skill;

    @Size(max = 64)
    @Column(name = "custom_skill", length = 64)
    private String customSkill;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateSkill candidateSkill)) return false;

        if (candidateSkill.getCandidate() == null) return false;
        if (candidateSkill.getSkill() != null)
            return Objects.equals(candidate, candidateSkill.getCandidate()) &&
                    Objects.equals(skill, candidateSkill.getSkill());
        return Objects.equals(candidate, candidateSkill.getCandidate()) &&
                Objects.equals(customSkill, candidateSkill.getCustomSkill());
    }

    @Override
    public int hashCode() {
        if (skill != null)
            return Objects.hash(candidate, skill);
        return Objects.hash(candidate, customSkill);
    }
}
