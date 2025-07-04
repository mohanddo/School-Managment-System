package com.mohand.SchoolManagmentSystem.request.chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateDocumentRequest {
    @NotBlank(message = "Video must a title")
    private String title;

    @NotNull(message = "You must specify if the document is free or not")
    private boolean isFree;

    @NotNull(message = "Request must have a course id")
    private Long courseId;

    @NotNull(message = "Request must have a chapter id")
    private Long chapterId;

    @NotNull(message = "Request must have a document id")
    private Long documentId;
}
