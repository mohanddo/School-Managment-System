package com.mohand.SchoolManagmentSystem.request.announcement;

import jakarta.validation.constraints.NotNull;

public record CreateOrUpdateAnnouncementRequest(@NotNull(message = "An announcement must have text") String text,
                                                @NotNull(message = "This request must have a course id") Long courseId,
                                                Long announcementId) {
}
