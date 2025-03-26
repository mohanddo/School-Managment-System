package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateOrUpdateCourseReviewRequest;
import com.mohand.SchoolManagmentSystem.response.course.CoursePreview;

import java.time.LocalDate;
import java.util.List;

public interface ICourseService {

    void create(CreateCourseRequest request, Long teacherId);
    void delete(Long courseId, Long teacherId);

    Course updateTitle(Long courseId, String newTitle);
    Course updateDescription(Long courseId, String newDescription);
    Course updatePrice(Long courseId, double price);
    Course updateDiscountPercentage(Long courseId, int discountPercentage);
    Course updateDiscountExpirationTime(Long courseId, LocalDate discountExpirationDate);

    List<CoursePreview> getAll(Long studentId);
    Course getById(Long id);


    void addOrRemoveCourseFromFavourite(Long studentId, Long courseId);

    void createOrUpdateCourseReview(CreateOrUpdateCourseReviewRequest request, Long studentId);
    void deleteCourseReview(Long courseId, Long studentId);

    void createOrUpdateAnnouncement(CreateOrUpdateAnnouncementRequest request, Long teacherId);
    void deleteAnnouncement(Long announcementId, Long courseId, Long teacherId);
}
