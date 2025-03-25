package com.mohand.SchoolManagmentSystem.response.course;

import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import com.mohand.SchoolManagmentSystem.response.teacher.TeacherPreview;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;

@Setter
@Getter
@RequiredArgsConstructor
public final class CoursePreview {
    private final long id;
    private final String title;
    private final String description;
    private final String imageUrl;
    private final String introductionVideoUrl;
    private final int numberOfStudents;
    private final int numberOfReviews;
    private final double rating;
    private final Integer discountPercentage;
    private final LocalDate discountExpirationDate;
    private final PricingModel pricingModel;
    private Boolean favourite;
    private final double numberOfHours;
    private final TeacherPreview teacher;
}