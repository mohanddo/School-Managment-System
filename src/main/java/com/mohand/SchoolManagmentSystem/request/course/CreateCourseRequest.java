package com.mohand.SchoolManagmentSystem.request.course;

import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import com.mohand.SchoolManagmentSystem.model.Teacher;

import java.time.LocalDate;
import java.util.Optional;

public record CreateCourseRequest(String title,
                                  String description,
                                  long teacherId,
                                  PricingModel pricingModel,
                                  double price,
                                  Optional<String> imageUrl,
                                  Optional<String> introductionVideoUrl) {
}
