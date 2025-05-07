package com.mohand.SchoolManagmentSystem.response.course;

import com.mohand.SchoolManagmentSystem.response.chapter.Chapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeacherCourse extends Course {
    private List<Announcement> announcements;
    private boolean ownsCourse;
}
