package com.mohand.SchoolManagmentSystem.request.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(@NotNull(message = "Course must have title") String title,
                                  @NotNull(message = "Course must have description") String description,
                                  @NotNull(message = "Course must have pricing model") String pricingModel,
                                  double price,
                                  String imageUrl,
                                  String introductionVideoUrl) {
    public PricingModel getPricingModelEnum() {
        return PricingModel.validatePricingModel(pricingModel);
    }
}
