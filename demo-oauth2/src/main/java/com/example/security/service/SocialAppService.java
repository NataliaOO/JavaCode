package com.example.security.service;

import com.example.security.model.Role;
import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(SocialAppService.class);

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // GitHub атрибуты
        String githubId = String.valueOf(attributes.get("id"));
        String login = (String) attributes.get("login");
        String email = (String) attributes.get("email");

        logger.info("OAuth2 authentication success. Provider=Github, githubId={}, login={}, email={}",
                githubId, login, email);

        // находим или создаём пользователя
        User user = userRepository.findByGithubId(githubId)
                .orElseGet(() -> {
                    Role role = login.equals("NataliaOO") ? Role.ADMIN : Role.USER;
                    logger.info("Creating new user '{}' with role={}", login, role);
                    User newUser = User.builder()
                            .githubId(githubId)
                            .username(login)
                            .email(email)
                            .role(role)
                            .build();
                    return userRepository.save(newUser);
                });


        logger.info("User '{}' logged in with role={}", user.getUsername(), user.getRole());
        Collection<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return new DefaultOAuth2User(
                authorities,
                attributes,
                "id"
        );
    }
}
