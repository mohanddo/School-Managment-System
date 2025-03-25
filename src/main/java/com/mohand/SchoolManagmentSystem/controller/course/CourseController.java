package com.mohand.SchoolManagmentSystem.controller.course;

import com.mohand.SchoolManagmentSystem.model.User;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.response.course.CoursePreview;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
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

    private final ICourseService courseService;

    @GetMapping("/all")
    private ResponseEntity<List<CoursePreview>> getAllCourses(@RequestParam Long studentId) {
        return ResponseEntity.ok(courseService.getAll(studentId));
    }

    @PostMapping("/addToFavorite/{courseId}")
    private ResponseEntity<String> addToFavourite(@PathVariable Long courseId, Authentication authentication) {
        Long studentId = ( (User) ( authentication.getPrincipal() ) ).getId();
        courseService.addCourseToFavourite(studentId, courseId);
        return ResponseEntity.ok("Added to favourite");
    }

    @PostMapping("/create")
    private ResponseEntity<String> createCourse(@RequestBody CreateCourseRequest request, Authentication authentication) {
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
}
