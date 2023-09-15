package com.example.userservice.controller;

import com.example.userservice.annotation.swagger.*;
import com.example.userservice.dto.request.UserEmailDto;
import com.example.userservice.dto.request.UserNameDto;
import com.example.userservice.dto.request.UserRegForAdminDto;
import com.example.userservice.dto.response.KafkaDeleteUserResponse;
import com.example.userservice.dto.response.KafkaUpdateEmailUserResponse;
import com.example.userservice.dto.response.UserInfoDto;
import com.example.userservice.enums.StatusTransaction;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.CandidateService;
import com.example.userservice.service.UserService;
import com.example.userservice.service.kafka.Producer;
import com.example.userservice.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.example.userservice.config.KafkaConfig.*;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "User controller.", description = "Work with user.")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
    private static final String PAGE = "0";
    private static final String SIZE = "10";
    private final UserService userService;
    private final CandidateService candidateService;
    private final UserMapper userMapper;
    private final Producer producer;

    @SwaggerCreateUserKeycloak
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid UserRegForAdminDto dto) {
        userService.register(dto);
    }

    @Operation(summary = "Get all users.", description = "Return all users from Keycloak.")
    @SwaggerGetAll
    @GetMapping
    public ResponseEntity<List<UserInfoDto>> findAll(@RequestParam(defaultValue = PAGE) int page,
                                                     @RequestParam(defaultValue = SIZE) int size) {
        var usersPage = userService.findAll(page, size)
                .map(userMapper::mapToUserInfo);
        return ResponseBuilder.build(usersPage);
    }

    @Operation(summary = "Get all users by name.", description = "Return all users from Keycloak by name.")
    @SwaggerGetAllByName
    @GetMapping(params = {"firstName", "lastName"})
    public ResponseEntity<List<UserInfoDto>> findAllByName(@RequestParam @NotBlank String firstName,
                                                           @RequestParam @NotBlank String lastName,
                                                           @RequestParam(defaultValue = PAGE) int page,
                                                           @RequestParam(defaultValue = SIZE) int size) {
        var usersPage = userService.findAllByFirstNameAndLastName(firstName, lastName, page, size)
                .map(userMapper::mapToUserInfo);
        return ResponseBuilder.build(usersPage);
    }

    @SwaggerGetUserByEmail
    @GetMapping(params = {"email"})
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDto findByEmail(@RequestParam @Email String email) {
        return userMapper.mapToUserInfo(userService.findUserByEmail(email));
    }

    @SwaggerUpdateFirstAndLastNames
    @PreAuthorize("(#userNameDto.email == #principal.name && hasRole('ROLE_CANDIDATE')) || hasRole('ROLE_ADMIN')")
    @PatchMapping("/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateName(@RequestBody @Valid UserNameDto userNameDto, Principal principal) {
        //TODO:Добавить транзакции
        userService.updateUser(userNameDto);
        candidateService.updateFullNameByEmail(userNameDto);
    }

    @SwaggerUpdateEmail
    @PreAuthorize("(#userEmailDto.email == #principal.name && hasRole('ROLE_CANDIDATE')) || hasRole('ROLE_ADMIN')")
    @PatchMapping("/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmail(@RequestBody @Valid UserEmailDto userEmailDto, Principal principal) {
        //TODO:обновление в тестинг сервисе
        //TODO: надо как-то продумать верификацию почты, что делать, если она не была сделана
        userService.updateEmail(userEmailDto);
        candidateService.updateEmail(userEmailDto);
        producer.sendMessageUserChange(DATABASE_USER_CHANGE, KEY_ID_UPDATE_EMAIL,
                new KafkaUpdateEmailUserResponse(userEmailDto.getEmail(), userEmailDto.getNewEmail(), StatusTransaction.SUCCESS));
    }

    @SwaggerDeleteUserByEmail
    @PreAuthorize("(#email == #principal.name && hasRole('ROLE_CANDIDATE')) || hasRole('ROLE_ADMIN')")
    @DeleteMapping(params = {"email"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam @Email String email, Principal principal) {
        //TODO: удаление из тестинг сервис
        userService.setUserEnabled(email, false);
        candidateService.setCandidateIsBlocked(email, true);
        producer.sendMessageUserChange(DATABASE_USER_CHANGE, KEY_ID_DELETE_USER, new KafkaDeleteUserResponse(email, StatusTransaction.SUCCESS));
    }
}