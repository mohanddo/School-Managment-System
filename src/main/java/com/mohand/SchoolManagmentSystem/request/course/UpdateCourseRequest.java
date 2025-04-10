package com.mohand.SchoolManagmentSystem.request.course;

import com.mohand.SchoolManagmentSystem.enums.PricingModel;
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

    @NotBlank(message = "Course must have a pricing model")
    private String pricingModel;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private int price;

    @Min(value = 0, message = "Discount percentage must be at least 0")
    @Max(value = 100, message = "Discount percentage must be at most 100")
    private int discountPercentage;

    @Future(message = "Discount expiration date must be in the future")
    private LocalDate discountExpirationDate;

    private String imageUrl;
    private String introductionVideoUrl;

    public PricingModel getPricingModelEnum() {
        return PricingModel.validatePricingModel(pricingModel);
    }

    @AssertTrue(message = "For free courses, the price must be 0. Either set price to 0 or select a different pricing model.")
    public boolean isFreeCourseValid() {
        return getPricingModelEnum() != PricingModel.FREE || price == 0;
    }

    @AssertTrue(message = "To mark this as a paid course, please set a price greater than 0 or change to FREE pricing model.")
    public boolean isPaidCourseValid() {
        return getPricingModelEnum() == PricingModel.FREE || price > 0;
    }

    @AssertTrue(message = "Price must be greater than 0")
    public boolean isPriceValid() {
        return price >= 0;
    }

    @AssertTrue(message = "Courses with a discount percentage equal to 0 can't have a discount expiration date")
    public boolean isDiscountValid() {
        return discountPercentage != 0 || discountExpirationDate == null;
    }
}
