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
@Operation(summary = "Create or update candidate profile by email.",
        description = "Create or update full profile in PostgreSQL by email.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok, successful operation",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Server Error",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
})
public @interface SwaggerCreateOrUpdateCandidate {
}
