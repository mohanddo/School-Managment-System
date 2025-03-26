package com.mohand.SchoolManagmentSystem.exception.announcement;

public class AnnouncementNotFoundException extends AnnouncementException {
    public AnnouncementNotFoundException(String announcementId, String courseId) {
        super("Course with id " + courseId + " does not have announcement with id " + announcementId);
    }

    public AnnouncementNotFoundException(String announcementId) {
        super("Announcement with id " + announcementId + " does not exist");
    }
}
