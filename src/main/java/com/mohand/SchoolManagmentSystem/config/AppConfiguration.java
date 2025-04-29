package com.mohand.SchoolManagmentSystem.config;

import com.mohand.SchoolManagmentSystem.model.chapter.Document;
import com.mohand.SchoolManagmentSystem.model.chapter.Resource;
import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import com.mohand.SchoolManagmentSystem.model.course.CourseReview;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.repository.CourseRepository;
import com.mohand.SchoolManagmentSystem.repository.CourseReviewRepository;
import com.mohand.SchoolManagmentSystem.repository.TeacherStudentRepository;
import com.mohand.SchoolManagmentSystem.repository.UserRepository;
import com.mohand.SchoolManagmentSystem.request.payment.CreateProductRequest;
import com.mohand.SchoolManagmentSystem.response.course.Course;
import com.mohand.SchoolManagmentSystem.response.course.StudentCourse;
import com.mohand.SchoolManagmentSystem.response.course.TeacherCourse;
import com.mohand.SchoolManagmentSystem.response.user.StudentPreview;
import com.mohand.SchoolManagmentSystem.response.user.TeacherPreview;
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

import java.net.http.HttpClient;
import java.util.List;

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
        private final CourseRepository courseRepository;
        private final TeacherStudentRepository teacherStudentRepository;
        private final CourseReviewRepository courseReviewRepository;

    public double calculateRating(Long courseId) {
        List<CourseReview> courseReviews = courseReviewRepository.findAllByCourseId(courseId);
        if(courseReviews.isEmpty()) {
            return 0;
        } else {
            double reviewsSum = courseReviews.stream().map((courseReview -> courseReview.getReview().getValue())).reduce(0.0, Double::sum);
            return reviewsSum / courseReviews.size();
        }
    }

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

            Converter<Long, Integer> courseIdToStudentCount = context ->
                courseRepository.countStudentsById(context.getSource());

            Converter<Long, Double> courseIdToRating = context -> calculateRating(context.getSource());


            modelMapper.typeMap(com.mohand.SchoolManagmentSystem.model.course.Course.class, Course.class).addMappings(mapper -> {
                mapper.using(urlToSignedUrl).map(com.mohand.SchoolManagmentSystem.model.course.Course::getImageUrl, Course::setImageUrl);
                mapper.using(urlToSignedUrl).map(com.mohand.SchoolManagmentSystem.model.course.Course::getIntroductionVideoUrl, Course::setIntroductionVideoUrl);
                mapper.using(courseIdToStudentCount).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, Course::setNumberOfStudents);
                mapper.using(courseIdToRating).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, Course::setRating);
            });

            modelMapper.typeMap(com.mohand.SchoolManagmentSystem.model.course.Course.class, StudentCourse.class).addMappings(mapper -> {
                mapper.using(urlToSignedUrl).map(com.mohand.SchoolManagmentSystem.model.course.Course::getImageUrl, Course::setImageUrl);
                mapper.using(urlToSignedUrl).map(com.mohand.SchoolManagmentSystem.model.course.Course::getIntroductionVideoUrl, Course::setIntroductionVideoUrl);
                mapper.using(courseIdToStudentCount).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, Course::setNumberOfStudents);
                mapper.using(courseIdToRating).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, Course::setRating);
            });

            modelMapper.typeMap(com.mohand.SchoolManagmentSystem.model.course.Course.class, TeacherCourse.class).addMappings(mapper -> {
                mapper.using(urlToSignedUrl).map(com.mohand.SchoolManagmentSystem.model.course.Course::getImageUrl, Course::setImageUrl);
                mapper.using(urlToSignedUrl).map(com.mohand.SchoolManagmentSystem.model.course.Course::getIntroductionVideoUrl, Course::setIntroductionVideoUrl);
                mapper.using(courseIdToStudentCount).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, Course::setNumberOfStudents);
                mapper.using(courseIdToRating).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, Course::setRating);
            });

            Converter<Long, Integer> teacherIdToStudentCount = context -> {
                return teacherStudentRepository.countByTeacherId(context.getSource());
            };

            modelMapper.typeMap(Teacher.class, TeacherPreview.class).addMappings(mapper -> {
                mapper.using(urlToSignedUrl).map(Teacher::getProfilePicDownloadUrl, TeacherPreview::setProfilePicDownloadUrl);
                mapper.using(teacherIdToStudentCount).map(Teacher::getId, TeacherPreview::setNumberOfStudents);
            });

            modelMapper.typeMap(Student.class, StudentPreview.class).addMappings(mapper -> {
                mapper.using(urlToSignedUrl).map(Student::getProfilePicDownloadUrl, StudentPreview::setProfilePicDownloadUrl);
            });

            Converter<String, String> containerNameToBaseUrl =
                    ctx -> azureStorageEndpoint + "/" + ctx.getSource();

            modelMapper.typeMap(Teacher.class, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class).addMappings(mapper -> {
                mapper.using(containerNameToBaseUrl).map(
                        Teacher::getContainerName, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setBaseUrl
                );
                mapper.using(teacherIdToStudentCount).map(
                        Teacher::getId, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setNumberOfStudents
                );
            });

            Converter<String, String[]> courseImageUrlToArray = (context) ->  {
                if (context.getSource() != null) {
                    return new String[] { azureBlobService.signBlobUrl(context.getSource(), false) };
                } else {
                    return new String[] {};
                }
            } ;


            modelMapper.typeMap(com.mohand.SchoolManagmentSystem.model.course.Course.class, CreateProductRequest.class).addMappings(mapping -> {
                mapping.map(com.mohand.SchoolManagmentSystem.model.course.Course::getTitle, CreateProductRequest::setName);
                mapping.using(courseImageUrlToArray).map(com.mohand.SchoolManagmentSystem.model.course.Course::getImageUrl, CreateProductRequest::setImages);
            });

            modelMapper.typeMap(Student.class, com.mohand.SchoolManagmentSystem.response.authentication.Student.class)
                    .addMappings(mapper -> mapper.skip(com.mohand.SchoolManagmentSystem.response.authentication.Student::setCourses));

            modelMapper.typeMap(Teacher.class, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class)
                    .addMappings(mapper -> mapper.skip(com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setCourses));


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


