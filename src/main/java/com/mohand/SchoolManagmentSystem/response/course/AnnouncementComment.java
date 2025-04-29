package com.mohand.SchoolManagmentSystem.response.course;

import com.mohand.SchoolManagmentSystem.response.user.StudentPreview;
import com.mohand.SchoolManagmentSystem.response.user.UserPreview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnnouncementComment {
    private Long id;
    private String text;
    private String dateOfCreation;
    private UserPreview user;
}
