package com.mohand.SchoolManagmentSystem.controller.chapter;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.request.chapter.*;
import com.mohand.SchoolManagmentSystem.service.chapter.IChapterService;
import com.mohand.SchoolManagmentSystem.service.resource.IResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("${api.prefix}/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final IResourceService resourceService;

    @PostMapping("/addVideo")
    public ResponseEntity<String> addVideo(@Valid @RequestBody AddVideoRequest request, Authentication authentication) {
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        resourceService.addVideo(request, teacher);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/addDocument")
    public ResponseEntity<String> addDocument(@Valid @RequestBody AddDocumentRequest request, Authentication authentication) {
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        resourceService.addDocument(request, teacher);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/updateVideo")
    public ResponseEntity<String> updateVideo(@Valid @RequestBody UpdateVideoRequest request, Authentication authentication) {
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        resourceService.updateVideo(request, teacher);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/updateDocument")
    public ResponseEntity<String> updateDocument(@Valid @RequestBody UpdateDocumentRequest request, Authentication authentication) {
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        resourceService.updateDocument(request, teacher);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/addFinishedResource/{courseId}/{chapterId}/{resourceId}")
    public ResponseEntity<String> addFinishedResource(@PathVariable Long courseId, @PathVariable Long chapterId, @PathVariable Long resourceId, Authentication authentication) {
        Student student = (Student) ( authentication.getPrincipal() );
        resourceService.addFinishedResource(resourceId, chapterId , courseId , student);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/deleteFinishedResource/{resourceId}")
    public ResponseEntity<String> deleteFinishedResource(@PathVariable Long resourceId, Authentication authentication) {
        Student student = (Student) ( authentication.getPrincipal() );
        resourceService.deleteFinishedResource(resourceId, student);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/updateVideoProgress")
    public ResponseEntity<String> updateDocument(@Valid @RequestBody UpdateVideoProgressRequest request, Authentication authentication) {
        Student student = (Student) ( authentication.getPrincipal() );
        resourceService.updateVideoProgress(student, request);
        return ResponseEntity.ok("Success");
    }

}
