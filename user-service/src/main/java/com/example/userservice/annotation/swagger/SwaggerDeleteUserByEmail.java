package com.example.userservice.annotation.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Delete user by email",
        description = "Delete user by email from Keycloak and if exist from PostgreSQL for user-service and his result from testing-service.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No content, successful operation"),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Server Error",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
public @interface SwaggerDeleteUserByEmail {
}
