package com.example.userservice.service.client;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeycloakRestTemplate {
    private final Keycloak keycloak;

    public RestTemplate oauth2RestTemplate() {
        TokenManager tokenmanager = keycloak.tokenManager();
        ClientHttpRequestInterceptor clientHttpRequestInterceptor = (request, body, execution) -> {
            AccessTokenResponse accessToken1 = tokenmanager.getAccessToken(); //only refreshes token when necessary
            String accessToken = accessToken1.getToken(); //get token as String
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            return execution.execute(request, body);
        };
        RestTemplate template = new RestTemplate();
        template.setInterceptors(List.of(clientHttpRequestInterceptor));
        return template;
    }
}
