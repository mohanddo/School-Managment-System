package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementCommentRequest;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateOrUpdateCourseReviewRequest;
import com.mohand.SchoolManagmentSystem.request.course.UpdateCourseRequest;
import com.mohand.SchoolManagmentSystem.response.course.Course;

import java.util.List;

public interface ICourseService {

    void update(UpdateCourseRequest request, Teacher teacher);

    List<Course> getAll();
    Course getCourseResponseById(Long id);
    List<com.mohand.SchoolManagmentSystem.model.course.Course> findAll();
    com.mohand.SchoolManagmentSystem.model.course.Course getById(Long id);


    void addCourseToFavourite(Student student, Long courseId);
    void removeCourseFromFavourite(Student student, Long courseId);
    void addCourseToCart(Student student, Long courseId);
    void removeCourseFromCart(Student student, Long courseId);

    void createOrUpdateCourseReview(CreateOrUpdateCourseReviewRequest request, Student student);
    void deleteCourseReview(Long courseId, Long studentId);

    void createOrUpdateAnnouncement(CreateOrUpdateAnnouncementRequest request, Long teacherId);
    void deleteAnnouncement(Long announcementId, Long courseId, Long teacherId);

    void createOrUpdateAnnouncementComment(CreateOrUpdateAnnouncementCommentRequest request, User user);
    void deleteAnnouncementComment(Long announcementId, Long courseId, Long commentId, User user);


    boolean existsByIdAndStudentId(Long id, Long studentId);
    boolean existsByIdAndTeacherId(Long id, Long teacherId);
    com.mohand.SchoolManagmentSystem.model.course.Course findByIdAndTeacherId(Long id, Long teacherId);

    List<com.mohand.SchoolManagmentSystem.model.course.Course> getAllCoursesByStudentId(Long studentId);
    List<com.mohand.SchoolManagmentSystem.model.course.Course> getAllCoursesByTeacherId(Long teacherId);

    void save(com.mohand.SchoolManagmentSystem.model.course.Course course);


    int countProgressPercentageByCourseIdAndStudentId(Long courseId, Long studentId);
}
