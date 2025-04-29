package com.mohand.SchoolManagmentSystem.response.course;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Announcement {
    private Long id;
    private String text;
    private LocalDateTime dateOfCreation;
    private List<AnnouncementComment> announcementComments;
}
