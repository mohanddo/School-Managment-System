package com.mohand.SchoolManagmentSystem.controller.comment;

import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementCommentRequest;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementRequest;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.DeleteCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.UpVoteCommentRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateOrUpdateCourseReviewRequest;
import com.mohand.SchoolManagmentSystem.request.course.UpdateCourseRequest;
import com.mohand.SchoolManagmentSystem.response.course.CoursePreview;
import com.mohand.SchoolManagmentSystem.service.comment.ICommentService;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("${api.prefix}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @PutMapping("/addOrUpdate")
    public ResponseEntity addOrUpdateComment(@RequestBody @Valid AddOrUpdateCommentRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.addOrUpdateComment(request, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteComment(@RequestBody @Valid DeleteCommentRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.deleteComment(request, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/upVote")
    public ResponseEntity upVoteComment(@RequestBody @Valid UpVoteCommentRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.upVoteComment(request, user);
        return ResponseEntity.ok().build();
    }
}
