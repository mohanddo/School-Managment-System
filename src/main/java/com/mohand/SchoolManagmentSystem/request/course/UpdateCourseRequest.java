package com.mohand.SchoolManagmentSystem.request.course;

import com.mohand.SchoolManagmentSystem.enums.CourseCategory;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCourseRequest {

    @NotNull(message = "Request must have a course id")
    private Long courseId;

    @NotBlank(message = "Course must have a title")
    private String title;

    @NotBlank(message = "Course must have a description")
    private String description;

    @NotBlank(message = "Course must have a category")
    private String category;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private int price;

    @Min(value = 0, message = "Discount percentage must be at least 0")
    @Max(value = 100, message = "Discount percentage must be at most 100")
    private int discountPercentage;

    @Future(message = "Discount expiration date must be in the future")
    private LocalDate discountExpirationDate;

    private String imageUrl;
    private String introductionVideoUrl;



    public CourseCategory getCategoryEnum() {
        return CourseCategory.validateCategory(category);
    }


    @AssertTrue(message = "Price must be greater than 0")
    public boolean isPriceValid() {
        return price >= 0;
    }


}
