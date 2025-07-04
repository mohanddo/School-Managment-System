package com.mohand.SchoolManagmentSystem.request.chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddVideoRequest {
    @NotBlank(message = "Video must a title")
    private String title;

    @NotNull(message = "Video must have a duration in seconds")
    private Integer duration;

    @NotNull(message = "You must specify if the video is free or not")
    private boolean isFree;

    @NotBlank(message = "Video must have a download url")
    private String downloadUrl;

    @NotNull(message = "Request must have a course id")
    private Long courseId;

    @NotNull(message = "Request must have a chapter id")
    private Long chapterId;
}
