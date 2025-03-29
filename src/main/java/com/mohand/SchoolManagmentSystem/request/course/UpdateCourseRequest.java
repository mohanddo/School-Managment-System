package com.mohand.SchoolManagmentSystem.request.course;

import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateCourseRequest(@NotBlank(message = "Course must have title") String title,
                                  @NotBlank(message = "Course must have description") String description,
                                  @NotBlank(message = "Course must have pricing model") String pricingModel,
                                  double price,
                                  int discountPercentage,
                                  LocalDate discountExpirationDate
                                  ) {
    public PricingModel getPricingModelEnum() {
        return PricingModel.validatePricingModel(pricingModel);
    }
}
