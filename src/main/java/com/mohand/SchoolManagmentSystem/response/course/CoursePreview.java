package com.mohand.SchoolManagmentSystem.response.course;

import com.mohand.SchoolManagmentSystem.enums.CourseCategory;
import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import com.mohand.SchoolManagmentSystem.model.course.CourseReview;
import com.mohand.SchoolManagmentSystem.response.teacher.TeacherPreview;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CoursePreview {
    private long id;
    private String title;
    private String description;
    private String imageUrl;
    private String introductionVideoUrl;
    private int numberOfStudents;
    private int numberOfReviews;
    private int price;
    private double numberOfHours;
    private Integer discountPercentage;
    private LocalDate discountExpirationDate;
    private PricingModel pricingModel;
    private CourseCategory category;
    private Double rating;
    private TeacherPreview teacher;
    private List<com.mohand.SchoolManagmentSystem.response.course.CourseReview> courseReviews;
    private Boolean favourite;
    private Boolean enrolled;
    private Boolean inCart;
    private int progressPercentage;
}