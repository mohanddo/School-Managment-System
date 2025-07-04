package com.mohand.SchoolManagmentSystem.request.chapter;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReorderResourcesRequest {
    @NotNull(message = "Request must have a resource id")
    private List<Long> orderedResourceIds;

    @NotNull(
            message = "Request must have a chapter id"
    )
    private Long chapterId;

    @NotNull(message = "Request must have a course id")
    private Long courseId;
}
