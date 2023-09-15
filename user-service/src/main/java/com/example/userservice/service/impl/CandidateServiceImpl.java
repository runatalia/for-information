package com.example.userservice.service.impl;

import com.example.userservice.dto.request.CandidateRequestDto;
import com.example.userservice.dto.request.UserEmailDto;
import com.example.userservice.dto.request.UserNameDto;
import com.example.userservice.dto.response.FullCandidateInfoResponseDto;
import com.example.userservice.dto.response.MainCandidateInfoResponseDto;
import com.example.userservice.entity.Candidate;
import com.example.userservice.mapper.CandidateMapper;
import com.example.userservice.repository.CandidateRepository;
import com.example.userservice.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Service
public class CandidateServiceImpl implements CandidateService {
    private static final String USER_NOT_FOUND = "User not found by email: %s.";
    private final CandidateRepository candidateRepository;
    private final UserService userCredentialsKeycloakService;
    private final CandidateMapper candidateMapper;
    private final CityService cityService;
    private final EducationService educationService;
    private final ExperienceService experienceService;
    private final SpecialtyService specialtyService;
    private final CandidateSkillService candidateSkillService;

    @Value("${upload.path}")
    private String upLoadPath;

    @Override
    public FullCandidateInfoResponseDto findByEmail(String email) {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.formatted(email)));
        return candidateMapper.toFullDTO(candidate);
    }

    @Override
    public Page<MainCandidateInfoResponseDto> findAllProfileUsers(Pageable pageable) {
        return candidateRepository.findAll(pageable)
                .map(candidateMapper::toMainDTO);
    }

    @Override
    public Page<MainCandidateInfoResponseDto> findAllByCountry(List<Short> countryIds, Pageable pageable) {
        return candidateRepository.findAllByCountyIds(countryIds, pageable)
                .map(candidateMapper::toMainDTO);
    }

    @Override
    public Page<MainCandidateInfoResponseDto> findAllBySpecialty(List<Short> specialtyIds, Pageable pageable) {
        return candidateRepository.findAllBySpecialtyIds(specialtyIds, pageable)
                .map(candidateMapper::toMainDTO);
    }

    public Page<MainCandidateInfoResponseDto> searchByEmailOrName(String searchQuery, Pageable pageable) {
        if (isEmail(searchQuery))
            return findAllByEmail(searchQuery, pageable).map(candidateMapper::toMainDTO);
        else
            return findAllByName(searchQuery, pageable).map(candidateMapper::toMainDTO);
    }

    public Page<Candidate> findAllByEmail(String email, Pageable pageable) {
        return candidateRepository.findAllByEmailIgnoreCase(email, pageable);
    }

    public Page<Candidate> findAllByName(String fullName, Pageable pageable) {
        return candidateRepository.findAllByFirstNameAndLastNameIgnoreCase(fullName, pageable);
    }

    @Override
    public void updateEmail(UserEmailDto userEmailDto) {
        candidateRepository.findByEmail(userEmailDto.getEmail())
                .ifPresent(userProfile -> {
                    candidateMapper.updateEmail(userProfile, userEmailDto);
                    candidateRepository.save(userProfile);
                });
    }

    public void updateFullNameByEmail(UserNameDto userNameDto) {
        candidateRepository.findByEmail(userNameDto.getEmail())
                .ifPresent(userProfile -> {
                            candidateMapper.updateName(userProfile, userNameDto);
                            candidateRepository.save(userProfile);
                        }
                );
    }

    @Override
    public void deleteByEmail(String email) {
        if (candidateRepository.existsByEmail(email))
            candidateRepository.deleteByEmail(email);
    }

    @Override
    public void setCandidateIsBlocked(String email, Boolean isBlocked) {
        candidateRepository.findByEmail(email)
                .ifPresent(candidate -> {
                    candidate.setIsBlocked(true);
                    candidateRepository.save(candidate);
                });
    }

    @Override
    @Transactional
    public FullCandidateInfoResponseDto createOrUpdateCandidateProfile(CandidateRequestDto candidateRequestDto, MultipartFile file) {
        var newCandidate = candidateMapper.fromDTO(candidateRequestDto);

        candidateRepository.findByEmail(candidateRequestDto.getEmail())
                .ifPresentOrElse(
                        oldCandidate -> updatePrepare(oldCandidate, newCandidate),
                        () -> createPrepare(newCandidate)
                );

        var city = cityService.createOrUpdateCity(newCandidate.getCity());
        var specialties = specialtyService.findAllById(candidateRequestDto.getSpecialityIds());
        var skills = candidateSkillService.createOrUpdateCascade(candidateRequestDto.getSkills(), newCandidate);
        var educations = educationService.createOrUpdateEducationsCascade(candidateRequestDto.getEducations(), newCandidate);
        var experiences = experienceService.createOrUpdateExperiencesCascade(candidateRequestDto.getExperiences(), newCandidate);

        candidateMapper.update(newCandidate, city, educations, skills, specialties, experiences);
        uploadFile(file, newCandidate);
        try {
            var savedCandidate = candidateRepository.save(newCandidate);
            var resultCandidateProfile = candidateMapper.toFullDTO(savedCandidate);
            resultCandidateProfile.setImage(getImageWithPath(resultCandidateProfile.getImage()));
            return resultCandidateProfile;
        } catch (Exception e) {
            log.error("%s user not saved", candidateRequestDto.getEmail());
            e.getStackTrace();
            deleteFile(file.getName());
            throw new InternalServerErrorException("%s user not saved".formatted(candidateRequestDto.getEmail()));
        }
    }

    private void updatePrepare(Candidate oldCandidate, Candidate newCandidate) {
        setNameCandidateProfile(newCandidate, oldCandidate.getFirstName(), oldCandidate.getLastName());
        newCandidate.setId(oldCandidate.getId());
    }

    private void createPrepare(Candidate candidate) {
        //if user not found in keycloak, would be exception
        var userCredentials = userCredentialsKeycloakService.findUserByEmail(candidate.getEmail());
        setNameCandidateProfile(candidate, userCredentials.getFirstName(), userCredentials.getLastName());
    }

    private void setNameCandidateProfile(Candidate candidate, String firstName, String lastName) {
        candidate.setFirstName(firstName);
        candidate.setLastName(lastName);
    }

    //TODO make a photo to cloud
    private void uploadFile(MultipartFile file, Candidate candidate) {
        if (file != null) {
            File uploadDir = new File(upLoadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            var index = file.getOriginalFilename().indexOf('.');
            String fileExtension = index == -1 ? "img" : file.getOriginalFilename().substring(index);
            String resultFileName = candidate.getEmail() + fileExtension;

            Path path = Paths.get(upLoadPath).toAbsolutePath();
            try {
                //TODO MultipartFile works only correctly in Controller. Here it takes the relative path of its server
                file.transferTo(new File(path.toFile(), resultFileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            candidate.setImage(resultFileName);
        }
    }

    private void deleteFile(String image) {
        File file = new File(image);
        if (file.exists()) {
            file.delete();
        }
    }

    private boolean isEmail(String input) {
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return input.matches(emailRegex);
    }

    private String getImageWithPath(String image) {
        return Paths.get(upLoadPath).toAbsolutePath() + "\\" + image;
    }
}
