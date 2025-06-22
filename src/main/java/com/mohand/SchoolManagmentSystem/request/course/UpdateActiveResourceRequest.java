package com.mohand.SchoolManagmentSystem.request.course;

import com.mohand.SchoolManagmentSystem.enums.CourseCategory;
import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateActiveResourceRequest {
    @NotNull(message = "Request must have a course id")
    private Long courseId;

    @NotNull(message = "Request must have a chapter id")
    private Long chapterId;

    @NotNull(message = "Request must have a resource id")
    private Long resourceId;
}
