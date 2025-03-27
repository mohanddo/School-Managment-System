package com.mohand.SchoolManagmentSystem.request.announcement;

import jakarta.validation.constraints.NotNull;

public record CreateOrUpdateAnnouncementCommentRequest(@NotNull String comment,
                                                       @NotNull Long announcementId,
                                                       @NotNull Long courseId,
                                                       Long commentId) {
}
