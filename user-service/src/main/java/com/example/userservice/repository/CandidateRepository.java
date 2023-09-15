package com.example.userservice.repository;


import com.example.userservice.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmail(String name);

    @Query(value = "SELECT c FROM Candidate c WHERE similarity(lower(c.email), lower(:email)) > 0.3")
    Page<Candidate> findAllByEmailIgnoreCase(@Param("email") String email, Pageable pageable);

    @Query(value = "SELECT c FROM Candidate c WHERE similarity(lower(c.firstName || ' ' || c.lastName), lower(:fullName)) > 0.3")
    Page<Candidate> findAllByFirstNameAndLastNameIgnoreCase(@Param("fullName") String fullName, Pageable pageable);

    @Query(value = "SELECT c FROM Candidate c WHERE c.city.country.id IN :countryIds")
    Page<Candidate> findAllByCountyIds(@Param("countryIds") List<Short> countryIds, Pageable pageable);

    @Query(value = "SELECT c FROM Candidate c JOIN c.specialties s WHERE s.id IN :specialtyIds")
    Page<Candidate> findAllBySpecialtyIds(@Param("specialtyIds") List<Short> specialtyIds, Pageable pageable);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}