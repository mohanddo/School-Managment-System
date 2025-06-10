package com.mohand.SchoolManagmentSystem.request.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpVoteReplyCommentRequest {

    @NotNull(message = "Request must have a course id")
    private Long courseId;

    @NotNull(message = "Request must have a chapter id")
    private Long chapterId;

    @NotNull(message = "Request must have a resource id")
    private Long resourceId;

    @NotNull(message = "Request must have a comment id")
    private Long commentId;

    @NotNull(message = "Request must have a reply comment id")
    private Long replyCommentId;
}
