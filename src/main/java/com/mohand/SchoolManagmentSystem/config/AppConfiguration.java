package com.mohand.SchoolManagmentSystem.config;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.StudentRepository;
import com.mohand.SchoolManagmentSystem.repository.UserRepository;
import com.mohand.SchoolManagmentSystem.request.payment.CreateProductRequest;
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
import java.net.http.HttpClient;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

        @Value("${api.prefix}")
        private String apiPrefix;

        @Value("${base.url}")
        private String baseUrl;

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

            Converter<String, String[]> courseImageUrlToArray = (context) ->  {
                if (context.getSource() != null) {
                    return new String[] { azureBlobService.signBlobUrl(context.getSource(), false) };
                } else {
                    return new String[] {};
                }
            } ;


            modelMapper.typeMap(Course.class, CreateProductRequest.class).addMappings(mapping -> {
                mapping.map(Course::getTitle, CreateProductRequest::setName);
                mapping.using(courseImageUrlToArray).map(Course::getImageUrl, CreateProductRequest::setImages);
            });

            return modelMapper;
        }

        @Bean
        public HttpClient httpClient() {
            return HttpClient.newHttpClient();
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


