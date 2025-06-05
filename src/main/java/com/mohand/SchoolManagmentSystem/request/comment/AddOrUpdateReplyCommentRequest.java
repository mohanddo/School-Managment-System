package com.mohand.SchoolManagmentSystem.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddOrUpdateReplyCommentRequest {
    @NotBlank(message = "Comment must have a text")
    private String text;
    private Long replyCommentId;
}
