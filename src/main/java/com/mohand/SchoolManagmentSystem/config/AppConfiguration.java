package com.mohand.SchoolManagmentSystem.config;

import com.mohand.SchoolManagmentSystem.repository.StudentRepository;
import com.mohand.SchoolManagmentSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

        @Value("${api.prefix}")
        private String apiPrefix;

        @Value("${base.url}")
        private String baseUrl;

        @Value("${chargily.pay.test.mode.secret.key}")
        private String chargilyPayTestModeSecretKey;

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }


    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://pay.chargily.net/test/api/v2/checkouts")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + chargilyPayTestModeSecretKey)
                .build();
    }

    private final UserRepository userRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}


