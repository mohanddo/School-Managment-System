package com.mohand.SchoolManagmentSystem.controller.course;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementCommentRequest;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateOrUpdateCourseReviewRequest;
import com.mohand.SchoolManagmentSystem.request.course.UpdateCourseRequest;
import com.mohand.SchoolManagmentSystem.response.course.Course;
import com.mohand.SchoolManagmentSystem.service.course.CourseService;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import com.mohand.SchoolManagmentSystem.service.teacher.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("${api.prefix}/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final TeacherService teacherService;

    @GetMapping("/all")
    private ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @GetMapping("/byId/{courseId}")
    private ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseResponseById(courseId));
    }


    @PostMapping("/addCourseToFavorite/{courseId}")
    private ResponseEntity<String> addCourseToFavourite(@PathVariable Long courseId, Authentication authentication) {
        Student student = ( (Student) ( authentication.getPrincipal() ) );
        courseService.addCourseToFavourite(student, courseId);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/removeCourseFromFavorite/{courseId}")
    private ResponseEntity<String> removeCourseFromFavourite(@PathVariable Long courseId, Authentication authentication) {
        Student student = ( (Student) ( authentication.getPrincipal() ) );
        courseService.removeCourseFromFavourite(student, courseId);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/addCourseToCart/{courseId}")
    private ResponseEntity<String> addOrRemoveCourseFromCart(@PathVariable Long courseId, Authentication authentication) {
        Student student = ( (Student) ( authentication.getPrincipal() ) );
        courseService.addCourseToCart(student, courseId);
        return ResponseEntity.ok("Success");
    }


    @DeleteMapping("/removeCourseFromCart/{courseId}")
    private ResponseEntity<String> removeCourseFromCart(@PathVariable Long courseId, Authentication authentication) {
        Student student = ( (Student) ( authentication.getPrincipal() ) );
        courseService.removeCourseFromCart(student, courseId);
        return ResponseEntity.ok("Success");
    }


    @PostMapping("/create")
    private ResponseEntity<Long> createCourse(@Valid @RequestBody CreateCourseRequest request, Authentication authentication) {
        return ResponseEntity.ok(teacherService.create(request, (Teacher) ( authentication.getPrincipal() )));
    }

    @PutMapping("/update")
    private ResponseEntity<String> updateCourse(@Valid @RequestBody UpdateCourseRequest request, Authentication authentication) {
        courseService.update(request, (Teacher) ( authentication.getPrincipal() ));
        return ResponseEntity.ok("Course updated successfully");
    }


    @PutMapping("/courseReview/createOrUpdate")
    private ResponseEntity<String> createCourseReview(Authentication authentication, @Valid @RequestBody CreateOrUpdateCourseReviewRequest request) {
        Student student = ( (Student) ( authentication.getPrincipal() ) );
        courseService.createOrUpdateCourseReview(request, student);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/courseReview/delete/{courseId}")
    private ResponseEntity<String> deleteCourseReview(Authentication authentication, @PathVariable Long courseId) {
        Long studentId = ( (User) ( authentication.getPrincipal() ) ).getId();
        courseService.deleteCourseReview(courseId, studentId);
        return ResponseEntity.ok("Course review deleted successfully");
    }

    @PutMapping("/announcement/createOrUpdate")
    private ResponseEntity<String> createOrUpdateAnnouncement(Authentication authentication, @Valid @RequestBody CreateOrUpdateAnnouncementRequest request) {
        Long teacherId = ( (User) ( authentication.getPrincipal() ) ).getId();
        courseService.createOrUpdateAnnouncement(request, teacherId);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/announcement/delete/{announcementId}/{courseId}")
    private ResponseEntity<String> deleteAnnouncement(Authentication authentication, @PathVariable Long announcementId, @PathVariable Long courseId) {
        Long teacherId = ( (User) ( authentication.getPrincipal() ) ).getId();
        courseService.deleteAnnouncement(announcementId, courseId, teacherId);
        return ResponseEntity.ok("Announcement deleted successfully");
    }

    @PutMapping("/announcementComment/createOrUpdate")
    private ResponseEntity<String> createOrUpdateAnnouncement(Authentication authentication, @Valid @RequestBody CreateOrUpdateAnnouncementCommentRequest request) {
        courseService.createOrUpdateAnnouncementComment(request, (User) authentication.getPrincipal());
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/announcementComment/delete/{commentId}/{announcementId}/{courseId}")
    private ResponseEntity<String> deleteAnnouncement(Authentication authentication, @PathVariable Long announcementId, @PathVariable Long courseId, @PathVariable Long commentId) {
        courseService.deleteAnnouncementComment(announcementId, courseId, commentId, (User) authentication.getPrincipal());
        return ResponseEntity.ok("Announcement deleted successfully");
    }
}
