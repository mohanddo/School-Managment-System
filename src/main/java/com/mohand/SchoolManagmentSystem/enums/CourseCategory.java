package com.mohand.SchoolManagmentSystem.enums;


import com.mohand.SchoolManagmentSystem.exception.BadRequestException;

public enum CourseCategory {
    MATH,
    SCIENCE,
    HISTORY,
    LANGUAGE,
    PROGRAMMING,
    PHYSICS;


    public static CourseCategory validateCategory(String category) {
        try {
            return CourseCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid course category: " + category);
        }
    }
}
