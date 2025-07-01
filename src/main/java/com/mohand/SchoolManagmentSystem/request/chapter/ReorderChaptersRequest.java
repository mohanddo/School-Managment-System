package com.mohand.SchoolManagmentSystem.request.chapter;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReorderChaptersRequest {
    @NotNull(message = "Request must have a chapter id")
    private List<Long> orderedChapterIds;

    @NotNull(message = "Request must have a course id")
    private Long courseId;
}
