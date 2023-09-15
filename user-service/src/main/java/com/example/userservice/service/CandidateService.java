package com.example.userservice.service;

import com.example.userservice.dto.request.CandidateRequestDto;
import com.example.userservice.dto.request.UserEmailDto;
import com.example.userservice.dto.request.UserNameDto;
import com.example.userservice.dto.response.FullCandidateInfoResponseDto;
import com.example.userservice.dto.response.MainCandidateInfoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidateService {

    FullCandidateInfoResponseDto createOrUpdateCandidateProfile(CandidateRequestDto candidateRequestDto, MultipartFile file);

    FullCandidateInfoResponseDto findByEmail(String email);

    Page<MainCandidateInfoResponseDto> findAllByCountry(List<Short> countryIds, Pageable pageable);

    Page<MainCandidateInfoResponseDto> findAllProfileUsers(Pageable pageable);

    Page<MainCandidateInfoResponseDto> findAllBySpecialty(List<Short> specialtyIds, Pageable pageable);

    Page<MainCandidateInfoResponseDto> searchByEmailOrName(String searchQuery, Pageable pageable);

    void updateEmail(UserEmailDto userEmailDto);

    void updateFullNameByEmail(UserNameDto userNameDto);

    void deleteByEmail(String email);

    void setCandidateIsBlocked(String email, Boolean isBlocked);
}
