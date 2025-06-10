package com.mohand.SchoolManagmentSystem.controller.comment;

import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateReplyCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.UpVoteCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.UpVoteReplyCommentRequest;
import com.mohand.SchoolManagmentSystem.service.comment.ICommentService;
import com.mohand.SchoolManagmentSystem.service.comment.IReplyCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("${api.prefix}/replyComment")
@RequiredArgsConstructor
public class ReplyCommentController {

    private final IReplyCommentService replyCommentService;

    @PutMapping("/addOrUpdate")
    public ResponseEntity addOrUpdateReplyComment(@RequestBody @Valid AddOrUpdateReplyCommentRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        replyCommentService.addOrUpdateReplyComment(request, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{replyCommentId}")
    public ResponseEntity deleteReplyComment(@PathVariable Long replyCommentId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        replyCommentService.deleteReplyComment(replyCommentId, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/upVote")
    public ResponseEntity upVoteComment(@RequestBody @Valid UpVoteReplyCommentRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        replyCommentService.upVoteReplyComment(request, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/removeUpVote/{commentId}")
    public ResponseEntity removeUpVoteComment(@PathVariable Long commentId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        replyCommentService.removeUpVoteReplyComment(commentId, user);
        return ResponseEntity.ok().build();
    }
}
