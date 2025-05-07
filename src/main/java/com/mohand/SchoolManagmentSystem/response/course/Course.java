package com.mohand.SchoolManagmentSystem.response.course;

import com.mohand.SchoolManagmentSystem.enums.CourseCategory;
import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import com.mohand.SchoolManagmentSystem.response.chapter.Chapter;
import com.mohand.SchoolManagmentSystem.response.user.TeacherPreview;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Course {
    protected long id;
    protected String title;
    protected String description;
    protected String imageUrl;
    protected String introductionVideoUrl;
    protected int numberOfStudents;
    protected int numberOfReviews;
    protected int price;
    protected double numberOfHours;
    protected Integer discountPercentage;
    protected LocalDate discountExpirationDate;
    protected PricingModel pricingModel;
    protected CourseCategory category;
    protected Double rating;
    protected TeacherPreview teacher;
    protected List<Chapter> chapters;
    protected int numberOfVideos;
    protected int numberOfDocuments;
    protected List<com.mohand.SchoolManagmentSystem.response.course.CourseReview> courseReviews;
}