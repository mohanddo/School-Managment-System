package com.mohand.SchoolManagmentSystem.config;

import com.mohand.SchoolManagmentSystem.enums.Role;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilterConfig jwtFilterConfig;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
              .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/v1/auth/logout",
                                        "/api/v1/auth/login",
                                        "/api/v1/auth/resend/**",
                                        "/api/v1/auth/verify",
                                        "/api/v1/auth/student/signup",

                                        // Only in development
                                        "/api/v1/auth/teacher/signup",
                                        "/api/v1/auth/admin/signup",

                                        "/api/v1/password/verifyEmail/**",
                                        "/api/v1/password/savePassword",
                                        "/api/v1/password/resetPassword",
                                        "/invalidLink.html",
                                        "/linkExpired.html",
                                        "/passwordChangedSuccessfully.html",
                                        "/api/v1/password/updatePassword",
                                        "/api/v1/payments/checkout",
                                        "/api/v1/course/all",
                                        "/api/v1/purchase/webhook"
                                ).permitAll()

//                                .requestMatchers("/api/v1/auth/teacher/signup", "/api/v1/auth/admin/signup", "/api/v1/admin/**")
//                                .hasAuthority(Role.ROLE_ADMIN.getValue())

                                .requestMatchers("/api/v1/course/create", "/api/v1/course/delete/**", "/api/v1/course/update",
                                        "/api/v1/course/announcement/createOrUpdate", "/api/v1/course/announcement/delete/**",
                                        "/api/v1/chapter/addOrUpdate",
                                        "/api/v1/resource/addVideo", "/api/v1/resource/addDocument",
                                        "/api/v1/resource/updateVideo", "/api/v1/resource/updateDocument", "/api/v1/teacher/**")
                                .hasAuthority(Role.ROLE_TEACHER.getValue())

                                .requestMatchers("/api/v1/course/addOrRemoveCourseFromFavorite/**",
                                        "/api/v1/course/addOrRemoveCourseFromCart/**",
                                        "/api/v1/course/courseReview/createOrUpdate",
                                        "/api/v1/course/courseReview/delete/**",
                                        "/api/v1/resource/addOrDeleteFinishedResource/**",
                                        "/api/v1/purchase/cart",
                                        "/api/v1/purchase/course/**",
                                        "/api/v1/student/**")
                                .hasAuthority(Role.ROLE_STUDENT.getValue()).

                                requestMatchers("/api/v1/course/announcementComment/createOrUpdate", "/api/v1/course/announcementComment/delete/**",
                                        "/api/v1/comment/addOrUpdate", "/api/v1/comment/delete/**", "/api/v1/comment/upVote")
                                .hasAnyAuthority(Role.ROLE_TEACHER.getValue(), Role.ROLE_STUDENT.getValue())

                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilterConfig.jwtAuthenticationFilter(jwtService, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
