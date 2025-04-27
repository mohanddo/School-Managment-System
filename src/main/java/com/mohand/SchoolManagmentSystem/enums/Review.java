package com.mohand.SchoolManagmentSystem.enums;

import com.mohand.SchoolManagmentSystem.exception.BadRequestException;
import lombok.Getter;

@Getter
public enum Review {
    ONE_STAR(1.0),
    ONE_HALF_STAR(1.5),
    TWO_STARS(2.0),
    TWO_HALF_STAR(2.5),
    THREE_STARS(3.0),
    THREE_HALF_STAR(3.5),
    FOUR_STARS(4.0),
    FOUR_HALF_STAR(4.5),
    FIVE_STARS(5.0);

    private final double value;

    Review(double value) {
        this.value = value;
    }

    public static Review validateReview(Double value) {

        for (Review r : values()) {
            if (r.value == value) {
                return r;
            }
        }
        throw new BadRequestException("Invalid review value: " + value);
    }
}
