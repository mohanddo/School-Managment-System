package com.mohand.SchoolManagmentSystem.controller.chapter;

import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.chapter.AddOrUpdateChapterRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.Student;
import com.mohand.SchoolManagmentSystem.service.chapter.IChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${api.prefix}/chapter")
@RequiredArgsConstructor
public class ChapterController {

    private final IChapterService chapterService;

    @PutMapping("/addOrUpdate")
    public ResponseEntity<String> addOrUpdateChapter(@Valid @RequestBody AddOrUpdateChapterRequest request, Authentication authentication) {
        Teacher teacher = (Teacher) ( authentication.getPrincipal() );
        chapterService.addOrUpdateChapter(request, teacher);
        return ResponseEntity.ok("Success");
    }
}
