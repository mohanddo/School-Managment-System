package com.mohand.SchoolManagmentSystem.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddOrUpdateCommentRequest {
    @NotBlank(message = "Comment must have a text")
    private String text;

    @NotNull(message = "Request must have a course id")
    private Long courseId;

    @NotNull(message = "Request must have a chapter id")
    private Long chapterId;

    @NotNull(message = "Request must have a resource id")
    private Long resourceId;

    private Long commentId;
}
