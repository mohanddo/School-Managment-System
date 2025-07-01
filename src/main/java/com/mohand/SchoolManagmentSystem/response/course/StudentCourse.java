package com.mohand.SchoolManagmentSystem.response.course;

import com.mohand.SchoolManagmentSystem.response.chapter.Resource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StudentCourse extends Course {
    private List<Announcement> announcements;
    private Boolean favourite;
    private Boolean enrolled;
    private Boolean inCart;
    private int progressPercentage;
    private Resource activeResource;
    private CourseReview studentReview;
}