package com.mohand.SchoolManagmentSystem.request.course;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateCourseRequest(String title,
                                  String description,
                                  String pricingModel,
                                  double price,
                                  String imageUrl,
                                  String introductionVideoUrl) {
}
