package com.example.userservice.service.client;

import com.example.userservice.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class KeycloakClient {
    private static final String DEFAULT_ERROR_MESS_KEYCLOAK = "Failed to operation with Keycloak API.";
    private static final String FORBIDDEN_MESS_KEYCLOAK =
            "No permissions for this operation with Keycloak API. Write to developer.";
    private static final String USER_NOT_FOUND_MESS = "User not found by email: %s.";
    private static final String USER_EXISTS_MESS = "User exists with same email.";
    private final UsersResource usersResource;
    private final RolesResource rolesResource;

    public void register(User user) {
        createUser(user);
        verifyEmail(user.getEmail());
        var roles = user
                .getRoles()
                .stream()
                .map(this::getRoleByName)
                .toList();
        assignRoles(roles, user.getEmail());
    }

    public void createUser(User userCredentials) {
        var password = preparePasswordRepresentation(userCredentials.getPassword(), false);
        var user = prepareUserRepresentation(userCredentials, password);
        try (var response = usersResource.create(user)) {
            if (response.getStatus() != HttpStatus.CREATED.value()) {
                checkResponseStatus(response);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(User user) {
        String email = user.getEmail();
        UserResource userResource = usersResource.get(getUserIdByUsername(email));

        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());

        userResource.update(userRepresentation);
    }

    public void verifyEmail(String email) {
        UserResource userResource = usersResource.get(getUserIdByUsername(email));
        userResource.sendVerifyEmail();
    }

    public RoleRepresentation getRoleByName(String name) {
        return rolesResource.get(name).toRepresentation();
    }

    public UserRepresentation findUserByEmail(String email) {
        List<UserRepresentation> users = usersResource.searchByUsername(email, true);

        if (users == null || users.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND_MESS.formatted(email));
        }

        return users.get(0);
    }

    public List<UserRepresentation> findAll(int page, int size) {
        return usersResource.list(page * size, size);
    }

    public int countOfAllUsers() {
        return usersResource.count();
    }

    public List<UserRepresentation> findAllByFirstAndLastName(String firstName, String lastName, int page, int size) {
        return usersResource.search(null, firstName, lastName, null, page * size,
                size, true, false, true);
    }

    public int countOfAllUsersByFirstAndLastName(String lastName, String firstName) {
        return usersResource.count(lastName, firstName, null, null);
    }

    public void assignRoles(List<RoleRepresentation> roleRepresentation, String email) {
        var id = getUserIdByUsername(email);
        usersResource
                .get(id)
                .roles()
                .realmLevel()
                .add(roleRepresentation);
    }



}