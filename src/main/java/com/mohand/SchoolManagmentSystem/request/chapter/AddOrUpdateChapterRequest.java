package com.mohand.SchoolManagmentSystem.request.chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddOrUpdateChapterRequest {
    @NotBlank(message = "Chapter must a title")
    private String title;

    @NotNull(message = "Request must have a course id")
    private Long courseId;
    private Long chapterId;
}
