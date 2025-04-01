package com.mohand.SchoolManagmentSystem.request.course;

import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {

    @NotBlank(message = "Course must have a title")
    private String title;

    @NotBlank(message = "Course must have a description")
    private String description;

    @NotNull(message = "Course must have a pricing model")
    private String pricingModel;

    public PricingModel getPricingModelEnum() {
        return PricingModel.validatePricingModel(pricingModel);
    }

    private double price;

    private String imageUrl;
    private String introductionVideoUrl;

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
}
