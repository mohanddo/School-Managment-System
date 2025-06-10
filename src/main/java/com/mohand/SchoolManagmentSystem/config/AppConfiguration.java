package com.mohand.SchoolManagmentSystem.config;

import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.comment.ReplyComment;
import com.mohand.SchoolManagmentSystem.model.course.CourseReview;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.request.payment.CreateProductRequest;
import com.mohand.SchoolManagmentSystem.response.course.Course;
import com.mohand.SchoolManagmentSystem.response.course.StudentCourse;
import com.mohand.SchoolManagmentSystem.response.course.TeacherCourse;
import com.mohand.SchoolManagmentSystem.response.user.StudentPreview;
import com.mohand.SchoolManagmentSystem.response.user.TeacherPreview;
import com.mohand.SchoolManagmentSystem.response.user.UserPreview;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.http.HttpClient;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

        @Value("${azure.storage.endpoint}")
        private String azureStorageEndpoint;

        private final AzureBlobService azureBlobService;
        private final CourseRepository courseRepository;
        private final TeacherStudentRepository teacherStudentRepository;
        private final CourseReviewRepository courseReviewRepository;
        private final UpVoteReplyCommentRepository upVoteReplyCommentRepository;
        private final UpVoteCommentRepository upVoteCommentRepository;


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
                    return azureBlobService.signBlobUrl(context.getSource());
                } else {
                    return null;
                }
            };

            Converter<Long, String > UserIdToSaSTokenWithReadPermission = context -> {

                if (context.getSource() != null) {

                    return azureBlobService.
                            generateSasTokenForBlob(azureStorageEndpoint + "/profilepics" + "/" + context.getSource());
                } else {
                    return null;
                }
            };

            Converter<Long, String > UserIdToSaSTokenWithWritePermission = context -> {

                if (context.getSource() != null) {
                    return azureBlobService.generateSasTokenForBlob(azureStorageEndpoint + "/profilepics" + "/" + context.getSource(), true);
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

            Converter<Long, Integer> teacherIdToStudentCount = context -> teacherStudentRepository.countByTeacherId(context.getSource());

            modelMapper.typeMap(Teacher.class, TeacherPreview.class).addMappings(mapper -> {
                mapper.using(UserIdToSaSTokenWithReadPermission).map(Teacher::getId, TeacherPreview::setSasTokenForReadingProfilePic);
                mapper.using(teacherIdToStudentCount).map(Teacher::getId, TeacherPreview::setNumberOfStudents);
            });



            modelMapper.typeMap(Student.class, StudentPreview.class).addMappings(mapper -> mapper.using(UserIdToSaSTokenWithReadPermission).map(Student::getId, StudentPreview::setSasTokenForReadingProfilePic));

            modelMapper.typeMap(User.class, UserPreview.class).addMappings(mapper -> mapper.using(UserIdToSaSTokenWithReadPermission).map(User::getId, UserPreview::setSasTokenForReadingProfilePic));

            Converter<String, String> containerNameToBaseUrl =
                    ctx -> azureStorageEndpoint + "/" + ctx.getSource();

            Converter<String, String> containerNameToSaSToken =
                    ctx -> azureBlobService.generateSASTokenForContainer(ctx.getSource());

            modelMapper.typeMap(Teacher.class, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class).addMappings(mapper -> {
                mapper.using(containerNameToBaseUrl).map(
                        Teacher::getContainerName, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setBaseUrl
                );
                mapper.using(teacherIdToStudentCount).map(
                        Teacher::getId, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setNumberOfStudents
                );
                mapper.using(containerNameToSaSToken).map(
                        Teacher::getContainerName, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setSasToken
                );
            });

            modelMapper.typeMap(Student.class, com.mohand.SchoolManagmentSystem.response.authentication.Student.class).addMappings(mapper -> {
                mapper.using(UserIdToSaSTokenWithReadPermission).map(Student::getId, com.mohand.SchoolManagmentSystem.response.authentication.Student::setSasTokenForReadingProfilePic);
                mapper.using(UserIdToSaSTokenWithWritePermission).map(Student::getId, com.mohand.SchoolManagmentSystem.response.authentication.Student::setSasTokenForWritingProfilePic);
            });

            modelMapper.typeMap(Teacher.class, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class).addMappings(mapper -> {
                mapper.using(UserIdToSaSTokenWithReadPermission).map(Teacher::getId, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setSasTokenForReadingProfilePic);
                mapper.using(UserIdToSaSTokenWithWritePermission).map(Teacher::getId, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setSasTokenForWritingProfilePic);
            });

            Converter<String, String[]> courseImageUrlToArray = (context) ->  {
                if (context.getSource() != null) {
                    return new String[] { azureBlobService.signBlobUrl(context.getSource()) };
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

            modelMapper.createTypeMap(Comment.class, com.mohand.SchoolManagmentSystem.response.course.Comment.class);
            modelMapper.typeMap(Comment.class, com.mohand.SchoolManagmentSystem.response.course.Comment.class)
                    .setPostConverter(ctx -> {
                        Comment source = ctx.getSource();
                        var dest = ctx.getDestination();

                        Long commentId = source.getId();

                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        Long currentUserId = ((User) auth.getPrincipal()).getId();

                        dest.setUpVotes(upVoteCommentRepository.countByCommentId(commentId));
                        dest.setHasCurrentUserUpVotedThisComment(
                                upVoteCommentRepository.existsByUserIdAndCommentId(currentUserId, commentId)
                        );

                        return dest;
                    });

            modelMapper.createTypeMap(ReplyComment.class, com.mohand.SchoolManagmentSystem.response.course.ReplyComment.class);
            modelMapper.typeMap(ReplyComment.class, com.mohand.SchoolManagmentSystem.response.course.ReplyComment.class)
                    .setPostConverter(ctx -> {
                        ReplyComment source = ctx.getSource();
                        var dest = ctx.getDestination();

                        Long commentId = source.getId();

                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        Long currentUserId = ((User) auth.getPrincipal()).getId();

                        dest.setUpVotes(upVoteReplyCommentRepository.countByReplyCommentId(commentId));
                        dest.setHasCurrentUserUpVotedThisReplyComment(
                                upVoteReplyCommentRepository.existsByUserIdAndReplyCommentId(currentUserId, commentId)
                        );

                        return dest;
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


