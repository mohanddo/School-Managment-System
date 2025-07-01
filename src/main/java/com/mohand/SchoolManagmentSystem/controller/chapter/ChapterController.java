package com.mohand.SchoolManagmentSystem.controller.chapter;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.request.chapter.AddOrUpdateChapterRequest;
import com.mohand.SchoolManagmentSystem.request.chapter.ReorderChaptersRequest;
import com.mohand.SchoolManagmentSystem.service.chapter.IChapterService;
import com.mohand.SchoolManagmentSystem.service.course.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("${api.prefix}/chapter")
@RequiredArgsConstructor
public class ChapterController {

    private final IChapterService chapterService;
    private final CourseService courseService;
    @PutMapping("/addOrUpdate")
    public ResponseEntity<String> addOrUpdateChapter(@Valid @RequestBody AddOrUpdateChapterRequest request, Authentication authentication) {
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        Course course = courseService.findByIdAndTeacherId(request.getCourseId(), teacher.getId());
        chapterService.addOrUpdateChapter(request, course);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/reorderChapters")
    public ResponseEntity<String> reorderChapters(@Valid @RequestBody ReorderChaptersRequest request, Authentication authentication){
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        chapterService.reorderChapters(request, teacher);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/deleteChapter/{courseId}/{chapterId}")
    public ResponseEntity<String> deleteChapter(@PathVariable Long courseId, @PathVariable Long chapterId, Authentication authentication){
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        chapterService.deleteChapter(courseId, chapterId, teacher);
        return ResponseEntity.ok("Success");
    }
}
