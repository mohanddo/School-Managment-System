package com.mohand.SchoolManagmentSystem.controller.comment;

import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.UpVoteCommentRequest;
import com.mohand.SchoolManagmentSystem.service.comment.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.deleteComment(commentId, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/upVote")
    public ResponseEntity upVoteComment(@RequestBody @Valid UpVoteCommentRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.upVoteComment(request, user);
        return ResponseEntity.ok().build();
    }
}
