package com.mohand.SchoolManagmentSystem.request.chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateResourceRequest {
    @NotBlank(message = "Resource must a title")
    private String title;

    @NotNull(message = "You must specify if the video is free or not")
    private boolean isFree;

    private Integer duration;

    @NotNull(message = "Request must have a course id")
    private Long courseId;

    @NotNull(message = "Request must have a chapter id")
    private Long chapterId;

    @NotNull(message = "Request must have a resource id")
    private Long resourceId;
}
