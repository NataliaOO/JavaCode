package com.example.security.config;

import com.example.security.service.OAuth2LogoutSuccessHandler;
import com.example.security.service.SocialAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SocialAppService socialAppService;
    private final OAuth2LogoutSuccessHandler oAuth2LogoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/error/**", "/css/**", "/js/**", "/webjars/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // обработка ошибок доступа
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/error/unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/error/forbidden");
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(socialAppService)
                        )
                        .defaultSuccessUrl("/user", true)
                        .failureHandler((request, response, exception) -> {
                            // сюда попадём, если:
                            // - пользователь отменил логин у GitHub
                            // - истёк/невалиден code/token
                            // - другая ошибка OAuth2
                            request.getSession().setAttribute("oauth2ErrorMessage", exception.getMessage());
                            response.sendRedirect("/error/oauth2");
                        })
                )

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(oAuth2LogoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
