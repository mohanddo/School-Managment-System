package com.mohand.SchoolManagmentSystem.request.announcement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrUpdateAnnouncementCommentRequest(@NotBlank(message = "Comment must be not null and not blank") String comment,
                                                       @NotNull Long announcementId,
                                                       @NotNull Long courseId,
                                                       Long commentId) {
}
