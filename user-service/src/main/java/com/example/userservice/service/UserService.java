package com.example.userservice.service;

import com.example.userservice.dto.request.UserEmailDto;
import com.example.userservice.dto.request.UserNameDto;
import com.example.userservice.dto.request.UserPasswordDto;
import com.example.userservice.dto.request.UserRegForAdminDto;
import com.example.userservice.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    void register(UserRegForAdminDto dto);

    User findUserByEmail(String email);

    void deleteUser(String email);

    Page<User> findAll(int page, int size);

    Page<User> findAllByFirstNameAndLastName(String firstName, String lastName, int page, int size);

    int countOfAllUsersByFirstNameAndLastName(String firstName, String lastName);

    void updateEmail(UserEmailDto userEmailDto);

    void forgotPassword(String email);

    void updateUser(UserNameDto userNameDto);

    void setTemporaryPassword(UserPasswordDto userPasswordDto);

    void setUserEnabled(String email, Boolean enabled);
}
