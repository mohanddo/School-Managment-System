package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementCommentRequest;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateOrUpdateCourseReviewRequest;
import com.mohand.SchoolManagmentSystem.request.course.UpdateCourseRequest;
import com.mohand.SchoolManagmentSystem.response.course.CoursePreview;

import java.util.List;

public interface ICourseService {

    void create(CreateCourseRequest request, Teacher teacher);
//    void delete(Long courseId, Teacher teacher);
    void update(UpdateCourseRequest request, Teacher teacher);

    List<CoursePreview> getAll();
    Course getById(Long id);


    void addOrRemoveCourseFromFavourite(Student student, Long courseId);
    void addOrRemoveCourseFromCart(Student student, Long courseId);

    void createOrUpdateCourseReview(CreateOrUpdateCourseReviewRequest request, Student student);
    void deleteCourseReview(Long courseId, Long studentId);

    void createOrUpdateAnnouncement(CreateOrUpdateAnnouncementRequest request, Long teacherId);
    void deleteAnnouncement(Long announcementId, Long courseId, Long teacherId);

    void createOrUpdateAnnouncementComment(CreateOrUpdateAnnouncementCommentRequest request, User user);
    void deleteAnnouncementComment(Long announcementId, Long courseId, Long commentId, User user);


    boolean existsByIdAndStudentId(Long id, Long studentId);
    boolean existsByIdAndTeacherId(Long id, Long teacherId);
    Course findByIdAndTeacherId(Long id, Long teacherId);

    double calculateRating(Long courseId);

    List<Course> getAllCoursesByStudentId(Long studentId);

    void save(Course course);
}
