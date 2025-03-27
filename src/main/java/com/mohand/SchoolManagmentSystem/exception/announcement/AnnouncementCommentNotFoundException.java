package com.mohand.SchoolManagmentSystem.exception.announcement;

public class AnnouncementCommentNotFoundException extends AnnouncementException {
    public AnnouncementCommentNotFoundException(String announcementId, String courseId) {
        super("Course with id " + courseId + " does not have announcement with id " + announcementId);
    }

    public AnnouncementCommentNotFoundException(String announcementId) {
        super("Announcement with id " + announcementId + " does not exist");
    }
}
