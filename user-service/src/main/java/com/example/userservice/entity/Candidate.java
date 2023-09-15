package com.example.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"educations", "experiences", "skills", "specialties"})
@EqualsAndHashCode(of = "email")
@Entity
@Table(name = "Candidate", schema = "users")
public class Candidate {
    @Id
    @SequenceGenerator(name = "candidate_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidate_sequence")
    private Long id;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "image", nullable = false)
    private String image;

    @Past
    @NotNull
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_city")
    private City city;

    @Column(name = "himself_description")
    private String himselfDescription;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Education> educations;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Experience> experiences;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CandidateSkill> skills;

    @NotNull
    @Builder.Default
    @Column(name = "is_blocked", nullable = false, columnDefinition = "default false")
    private Boolean isBlocked = false;

    @NotNull
    @NotEmpty
    @ManyToMany
    @JoinTable(name = "candidate_specialty",
            joinColumns = @JoinColumn(name = "id_candidate"),
            inverseJoinColumns = @JoinColumn(name = "id_specialty")
    )
    private Set<Specialty> specialties;


}