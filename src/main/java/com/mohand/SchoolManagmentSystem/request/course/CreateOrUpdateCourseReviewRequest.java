package com.mohand.SchoolManagmentSystem.request.course;

import com.mohand.SchoolManagmentSystem.enums.Review;
import jakarta.validation.constraints.NotNull;

public record CreateOrUpdateCourseReviewRequest(String comment,
                                                @NotNull Double review,
                                                @NotNull(message = "Request must have a course id") Long courseId) {
    public Review getReviewEnum() {
        return Review.validateReview(review);
    }
}
