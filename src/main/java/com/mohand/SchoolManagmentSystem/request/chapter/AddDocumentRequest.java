package com.mohand.SchoolManagmentSystem.request.chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddDocumentRequest {
    @NotBlank(message = "Video must a title")
    private String title;

    @NotBlank(message = "Video must have a download url")
    private String downloadUrl;

    @NotNull(message = "Request must have a course id")
    private Long courseId;

    @NotNull(message = "Request must have a chapter id")
    private Long chapterId;
}
