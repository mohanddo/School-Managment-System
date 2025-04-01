package com.mohand.SchoolManagmentSystem.config;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.StudentRepository;
import com.mohand.SchoolManagmentSystem.repository.UserRepository;
import com.mohand.SchoolManagmentSystem.response.course.CoursePreview;
import com.mohand.SchoolManagmentSystem.response.teacher.TeacherPreview;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
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

import java.awt.*;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

        @Value("${api.prefix}")
        private String apiPrefix;

        @Value("${base.url}")
        private String baseUrl;

        @Value("${chargily.pay.test.mode.secret.key}")
        private String chargilyPayTestModeSecretKey;

        @Value("${azure.storage.endpoint}")
        private String azureStorageEndpoint;

        private final AzureBlobService azureBlobService;

        @Bean
        public ModelMapper modelMapper() {
            ModelMapper modelMapper = new ModelMapper();

            Converter<String, String > urlToSignedUrl = context -> {

                if (context.getSource() != null) {
                    return azureBlobService.signBlobUrl(context.getSource(), false);
                } else {
                    return null;
                }
            };

            modelMapper.typeMap(Course.class, CoursePreview.class).addMappings(mapper -> {
                mapper.using(urlToSignedUrl).map(Course::getImageUrl, CoursePreview::setImageUrl);
                mapper.using(urlToSignedUrl).map(Course::getIntroductionVideoUrl, CoursePreview::setIntroductionVideoUrl);
            });

            modelMapper.typeMap(Teacher.class, TeacherPreview.class).addMappings(mapper -> {
                mapper.using(urlToSignedUrl).map(Teacher::getProfilePicDownloadUrl, TeacherPreview::setProfilePicDownloadUrl);
            });

            Converter<String, String> containerNameToBaseUrl =
                    ctx -> azureStorageEndpoint + "/" + ctx.getSource();

            modelMapper.typeMap(Teacher.class, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class).addMappings(mapper ->
                    mapper.using(containerNameToBaseUrl).map(
                            Teacher::getContainerName, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setBaseUrl
                    )
            );

            return modelMapper;
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


