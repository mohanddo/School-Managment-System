package com.mohand.SchoolManagmentSystem.controller.course;

import com.mohand.SchoolManagmentSystem.model.User;
import com.mohand.SchoolManagmentSystem.model.course.CourseReview;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateOrUpdateCourseReviewRequest;
import com.mohand.SchoolManagmentSystem.response.course.CoursePreview;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("${api.prefix}/course")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @GetMapping("/all")
    private ResponseEntity<List<CoursePreview>> getAllCourses(@RequestParam Long studentId) {
        return ResponseEntity.ok(courseService.getAll(studentId));
    }

    @PutMapping("/addOrRemoveCourseFromFavorite/{courseId}")
    private ResponseEntity<String> addOrRemoveCourseFromFavourite(@PathVariable Long courseId, Authentication authentication) {
        Long studentId = ( (User) ( authentication.getPrincipal() ) ).getId();
        courseService.addOrRemoveCourseFromFavourite(studentId, courseId);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/create")
    private ResponseEntity<String> createCourse(@Valid @RequestBody CreateCourseRequest request, Authentication authentication) {
        Long teacherId = ( (User) ( authentication.getPrincipal() ) ).getId();
        courseService.create(request, teacherId);
        return ResponseEntity.ok("Course created successfully");
    }

    @DeleteMapping("/delete/{courseId}")
    private ResponseEntity<String> deleteCourse(Authentication authentication, @PathVariable Long courseId) {
        Long teacherId = ( (User) ( authentication.getPrincipal() ) ).getId();
        courseService.delete(courseId, teacherId);
        return ResponseEntity.ok("Course deleted successfully");
    }

    @PutMapping("/courseReview/createOrUpdate")
    private ResponseEntity<String> createCourseReview(Authentication authentication, @Valid @RequestBody CreateOrUpdateCourseReviewRequest request) {
        Long studentId = ( (User) ( authentication.getPrincipal() ) ).getId();
        courseService.createOrUpdateCourseReview(request, studentId);
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
}
