package com.mohand.SchoolManagmentSystem.controller.chapter;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.request.chapter.*;
import com.mohand.SchoolManagmentSystem.request.course.UpdateActiveResourceRequest;
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

    @PutMapping("/updateResource")
    public ResponseEntity<String> updateDocument(@Valid @RequestBody UpdateResourceRequest request, Authentication authentication) {
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        resourceService.updateResource(request, teacher);
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
    public ResponseEntity<String> updateVideoProgress(@Valid @RequestBody UpdateVideoProgressRequest request, Authentication authentication) {
        Student student = (Student) ( authentication.getPrincipal() );
        resourceService.updateVideoProgress(student, request);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/updateActiveResource")
    public ResponseEntity<String> updateActiveResource(@Valid @RequestBody UpdateActiveResourceRequest request, Authentication authentication) {
        Student student = (Student) ( authentication.getPrincipal() );
        resourceService.updateActiveResource(request, student);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/reorderResources")
    public ResponseEntity<String> reorderResources(@Valid @RequestBody ReorderResourcesRequest request, Authentication authentication){
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        resourceService.reorderResources(request, teacher);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/deleteResource/{courseId}/{chapterId}/{resourceId}")
    public ResponseEntity<String> deleteResource(@PathVariable Long courseId, @PathVariable Long chapterId, @PathVariable Long resourceId, Authentication authentication){
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        resourceService.deleteResource(courseId, chapterId, resourceId, teacher);
        return ResponseEntity.ok("Success");
    }
}
