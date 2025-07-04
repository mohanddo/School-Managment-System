package com.mohand.SchoolManagmentSystem.config;

import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.comment.ReplyComment;
import com.mohand.SchoolManagmentSystem.model.course.CourseReview;
import com.mohand.SchoolManagmentSystem.model.course.CurrentResource;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.request.payment.CreateProductRequest;
import com.mohand.SchoolManagmentSystem.response.chapter.Resource;
import com.mohand.SchoolManagmentSystem.response.course.Course;
import com.mohand.SchoolManagmentSystem.response.course.StudentCourse;
import com.mohand.SchoolManagmentSystem.response.course.TeacherCourse;
import com.mohand.SchoolManagmentSystem.response.user.StudentPreview;
import com.mohand.SchoolManagmentSystem.response.user.TeacherPreview;
import com.mohand.SchoolManagmentSystem.response.user.UserPreview;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfiguration {

    @Value("${azure.storage.endpoint}")
        private String azureStorageEndpoint;

        private final AzureBlobService azureBlobService;
        private final CourseRepository courseRepository;
        private final TeacherStudentRepository teacherStudentRepository;
        private final CourseReviewRepository courseReviewRepository;
        private final UpVoteReplyCommentRepository upVoteReplyCommentRepository;
        private final UpVoteCommentRepository upVoteCommentRepository;
        private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final CurrentResourceRepository currentResourceRepository;


    private Converter<String, String> urlToSignedUrl;
    private Converter<Long, String> userIdToReadToken;
    private Converter<Long, String> userIdToWriteToken;
    private Converter<Long, Integer> courseIdToStudentCount;
    private Converter<Long, Integer> teacherIdToStudentCount;
    private Converter<Long, Double> courseIdToRating;
    private Converter<String, String[]> courseImageUrlToArray;
    private Converter<String, String> containerNameToBaseUrl;
    private Converter<String, String> containerNameToSas;
    private Converter<Long, com.mohand.SchoolManagmentSystem.response.chapter.Resource> studentIdAndCourseIdToResource;
    private Converter<Long, com.mohand.SchoolManagmentSystem.response.course.CourseReview> studentIdAndCourseIdToReview;
    private  Converter<Long, Boolean> teacherIdAndCourseIdToOwnsCourse;


    public double calculateRating(Long courseId) {
        List<CourseReview> courseReviews = courseReviewRepository.findAllByCourseId(courseId);
        if(courseReviews.isEmpty()) {
            return 0;
        } else {
            double reviewsSum = courseReviews.stream().map((courseReview -> courseReview.getReview().getValue())).reduce(0.0, Double::sum);
            return reviewsSum / courseReviews.size();
        }
    }

    private void createConverters(ModelMapper modelMapper) {
        Converter<String, String> urlToSignedUrl = ctx -> ctx.getSource() != null ? azureBlobService.signBlobUrl(ctx.getSource()) : null;

        Converter<Long, String> userIdToReadToken = ctx -> ctx.getSource() != null
                ? azureBlobService.generateSasTokenForBlob(azureStorageEndpoint + "/profilepics/" + ctx.getSource())
                : null;

        Converter<Long, String> userIdToWriteToken = ctx -> ctx.getSource() != null
                ? azureBlobService.generateSasTokenForBlob(azureStorageEndpoint + "/profilepics/" + ctx.getSource(), true)
                : null;

        Converter<Long, Integer> courseIdToStudentCount = ctx -> courseRepository.countStudentsById(ctx.getSource());
        Converter<Long, Integer> teacherIdToStudentCount = ctx -> teacherStudentRepository.countByTeacherId(ctx.getSource());
        Converter<Long, Double> courseIdToRating = ctx -> calculateRating(ctx.getSource());

        Converter<String, String[]> courseImageUrlToArray = ctx -> ctx.getSource() != null
                ? new String[]{azureBlobService.signBlobUrl(ctx.getSource())}
                : new String[]{};

        Converter<String, String> containerNameToBaseUrl = ctx -> azureStorageEndpoint + "/" + ctx.getSource();
        Converter<String, String> containerNameToSas = ctx -> azureBlobService.generateSASTokenForContainer(ctx.getSource());

        Converter<Long, Resource> studentIdAndCourseIdToResource = (ctx) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();

            if (principal instanceof Student student) {
                CurrentResource currentResource = currentResourceRepository.findByStudentIdAndCourseId(student.getId(), ctx.getSource());
                if (currentResource != null) {
                    Resource activeResource = modelMapper.map(currentResource.getResource(), Resource.class);
                    activeResource.setDownloadUrl(activeResource.getDownloadUrl() != null ? azureBlobService.signBlobUrl(activeResource.getDownloadUrl()) : null);
                    return activeResource;
                }
            }
            return null;
        };

        Converter<Long, Boolean> teacherIdAndCourseIdToOwnsCourse = (ctx) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();

            if (principal instanceof Teacher teacher) {
                return courseRepository.existsByIdAndTeacherId(ctx.getSource(),teacher.getId());
            }
            return null;
        };



        Converter<Long, com.mohand.SchoolManagmentSystem.response.course.CourseReview> studentIdAndCourseIdToReview = (ctx) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();

            if (principal instanceof Student student) {
                Optional<CourseReview> courseReview = courseReviewRepository.findByStudentIdAndCourseId(student.getId(), ctx.getSource());
                if (courseReview.isPresent()) {
                    return modelMapper.map(courseReview.get(), com.mohand.SchoolManagmentSystem.response.course.CourseReview.class);
                }
            }
            return null;
        };



        // Register converters to instance variables or reuse them in other methods
        this.urlToSignedUrl = urlToSignedUrl;
        this.userIdToReadToken = userIdToReadToken;
        this.userIdToWriteToken = userIdToWriteToken;
        this.courseIdToStudentCount = courseIdToStudentCount;
        this.teacherIdToStudentCount = teacherIdToStudentCount;
        this.courseIdToRating = courseIdToRating;
        this.courseImageUrlToArray = courseImageUrlToArray;
        this.containerNameToBaseUrl = containerNameToBaseUrl;
        this.containerNameToSas = containerNameToSas;
        this.studentIdAndCourseIdToResource = studentIdAndCourseIdToResource;
        this.studentIdAndCourseIdToReview = studentIdAndCourseIdToReview;
        this.teacherIdAndCourseIdToOwnsCourse = teacherIdAndCourseIdToOwnsCourse;
    }


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Create converters
        createConverters(modelMapper);

        // Register grouped mappings
        registerCourseMappings(modelMapper);
        registerUserMappings(modelMapper);
        registerAuthMappings(modelMapper);
        registerCommentMappings(modelMapper);
        registerChapterMappings(modelMapper);

        return modelMapper;
    }

    private void registerCourseMappings(ModelMapper modelMapper) {
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
            mapper.using(studentIdAndCourseIdToResource).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, StudentCourse::setActiveResource);
            mapper.using(studentIdAndCourseIdToReview).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, StudentCourse::setStudentReview);
        });

        modelMapper.typeMap(com.mohand.SchoolManagmentSystem.model.course.Course.class, TeacherCourse.class).addMappings(mapper -> {
            mapper.using(urlToSignedUrl).map(com.mohand.SchoolManagmentSystem.model.course.Course::getImageUrl, Course::setImageUrl);
            mapper.using(urlToSignedUrl).map(com.mohand.SchoolManagmentSystem.model.course.Course::getIntroductionVideoUrl, Course::setIntroductionVideoUrl);
            mapper.using(courseIdToStudentCount).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, Course::setNumberOfStudents);
            mapper.using(courseIdToRating).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, Course::setRating);
            mapper.using(teacherIdAndCourseIdToOwnsCourse).map(com.mohand.SchoolManagmentSystem.model.course.Course::getId, TeacherCourse::setOwnsCourse);
            mapper.skip(TeacherCourse::setAnnouncements);
        });



        modelMapper.typeMap(com.mohand.SchoolManagmentSystem.model.course.Course.class, CreateProductRequest.class).addMappings(mapping -> {
            mapping.map(com.mohand.SchoolManagmentSystem.model.course.Course::getTitle, CreateProductRequest::setName);
            mapping.using(courseImageUrlToArray).map(com.mohand.SchoolManagmentSystem.model.course.Course::getImageUrl, CreateProductRequest::setImages);
        });
    }

    private void registerUserMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(Teacher.class, TeacherPreview.class).addMappings(mapper -> {
            mapper.using(userIdToReadToken).map(Teacher::getId, TeacherPreview::setSasTokenForReadingProfilePic);
            mapper.using(teacherIdToStudentCount).map(Teacher::getId, TeacherPreview::setNumberOfStudents);
        });

        modelMapper.typeMap(Student.class, StudentPreview.class).addMappings(mapper -> mapper.using(userIdToReadToken).map(Student::getId, StudentPreview::setSasTokenForReadingProfilePic));

        modelMapper.typeMap(User.class, UserPreview.class).addMappings(mapper -> mapper.using(userIdToReadToken).map(User::getId, UserPreview::setSasTokenForReadingProfilePic));
    }

    private void registerAuthMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(Teacher.class, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class).addMappings(mapper -> {
            mapper.using(containerNameToBaseUrl).map(Teacher::getContainerName, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setBaseUrl);
            mapper.using(containerNameToSas).map(Teacher::getContainerName, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setSasToken);
            mapper.using(teacherIdToStudentCount).map(Teacher::getId, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setNumberOfStudents);
            mapper.using(userIdToReadToken).map(Teacher::getId, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setSasTokenForReadingProfilePic);
            mapper.using(userIdToWriteToken).map(Teacher::getId, com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setSasTokenForWritingProfilePic);
        });

        modelMapper.typeMap(Student.class, com.mohand.SchoolManagmentSystem.response.authentication.Student.class).addMappings(mapper -> {
            mapper.using(userIdToReadToken).map(Student::getId, com.mohand.SchoolManagmentSystem.response.authentication.Student::setSasTokenForReadingProfilePic);
            mapper.using(userIdToWriteToken).map(Student::getId, com.mohand.SchoolManagmentSystem.response.authentication.Student::setSasTokenForWritingProfilePic);
        });

        modelMapper.typeMap(Student.class, com.mohand.SchoolManagmentSystem.response.authentication.Student.class)
                .addMappings(mapper -> mapper.skip(com.mohand.SchoolManagmentSystem.response.authentication.Student::setCourses));

        modelMapper.typeMap(Teacher.class, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class)
                .addMappings(mapper -> mapper.skip(com.mohand.SchoolManagmentSystem.response.authentication.Teacher::setCourses));
    }

    private void registerCommentMappings(ModelMapper modelMapper) {
        modelMapper.createTypeMap(Comment.class, com.mohand.SchoolManagmentSystem.response.course.Comment.class)
                .setPostConverter(ctx -> {
                    Comment source = ctx.getSource();
                    var dest = ctx.getDestination();
                    Long commentId = source.getId();

                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    Object principal = auth.getPrincipal();

                    if (principal instanceof User user) {
                        Long currentUserId = user.getId();
                        dest.setUpVotes(upVoteCommentRepository.countByCommentId(commentId));
                        dest.setHasCurrentUserUpVotedThisComment(
                                upVoteCommentRepository.existsByUserIdAndCommentId(currentUserId, commentId));
                        dest.setUserOwnsThisComment(commentRepository.existsByIdAndUserId(commentId, currentUserId));
                    }
                    return dest;
                });

        modelMapper.createTypeMap(ReplyComment.class, com.mohand.SchoolManagmentSystem.response.course.ReplyComment.class)
                .setPostConverter(ctx -> {
                    ReplyComment source = ctx.getSource();
                    var dest = ctx.getDestination();
                    Long replyId = source.getId();

                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    Object principal = auth.getPrincipal();

                    if (principal instanceof User user) {
                        Long currentUserId = user.getId();
                        dest.setUpVotes(upVoteReplyCommentRepository.countByReplyCommentId(replyId));
                        dest.setHasCurrentUserUpVotedThisReplyComment(
                                upVoteReplyCommentRepository.existsByUserIdAndReplyCommentId(currentUserId, replyId));
                        dest.setUserOwnsThisReplyComment(replyCommentRepository.existsByIdAndUserId(replyId, currentUserId));
                    }
                    return dest;
                });
    }

    private void registerChapterMappings(ModelMapper modelMapper) {

        TypeMap<
                        com.mohand.SchoolManagmentSystem.model.chapter.Video,
                        Resource
                        > typeMap = modelMapper.createTypeMap(
                com.mohand.SchoolManagmentSystem.model.chapter.Video.class,
                com.mohand.SchoolManagmentSystem.response.chapter.Resource.class
        );

        typeMap.addMappings(mapper -> {
            mapper.skip(Resource::setVideoProgress);
        });

        modelMapper.createTypeMap(com.mohand.SchoolManagmentSystem.model.chapter.Chapter.class, com.mohand.SchoolManagmentSystem.response.chapter.Chapter.class)
                .addMappings(mapper ->
                        mapper.skip(com.mohand.SchoolManagmentSystem.response.chapter.Chapter::setResources));
    }
}


