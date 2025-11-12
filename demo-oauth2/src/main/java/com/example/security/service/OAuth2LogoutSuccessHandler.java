package com.example.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LogoutSuccessHandler implements LogoutSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();
            String principalName = oauthToken.getName();
            log.info("Logout requested. registrationId={}, principal={}", registrationId, principalName);
            OAuth2AuthorizedClient client =
                    authorizedClientService.loadAuthorizedClient(registrationId, principalName);

            if (client != null) {
                OAuth2AccessToken accessToken = client.getAccessToken();
                ClientRegistration clientRegistration = client.getClientRegistration();
                revokeTokenForGithub(clientRegistration, accessToken);

                authorizedClientService.removeAuthorizedClient(registrationId, principalName);
                log.info("Authorized client removed from local store. registrationId={}, principal={}",
                        registrationId, principalName);
            }
        }
        response.sendRedirect("/");
    }

    private void revokeTokenForGithub(ClientRegistration clientRegistration,
                                      OAuth2AccessToken accessToken) {
        try {
            String clientId = clientRegistration.getClientId();
            String clientSecret = clientRegistration.getClientSecret();
            String tokenValue = accessToken.getTokenValue();

            String url = "https://api.github.com/applications/" + clientId + "/grant";

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(clientId, clientSecret);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of("access_token", tokenValue);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Void> response =
                    restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

            log.info("GitHub token revoke request finished with HTTP status={}", response.getStatusCode());
        } catch (Exception e) {
            log.warn("Failed to revoke GitHub token", e);
        }
    }
}
