package com.mohand.SchoolManagmentSystem.enums;

import com.mohand.SchoolManagmentSystem.exception.InvalidEnumValueException;

public enum PricingModel {
    SUBSCRIPTION,
    ONE_TIME_PURCHASE,
    FREE;

    public static PricingModel validatePricingModel(String pricingModel) {
        try {
            return PricingModel.valueOf(pricingModel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumValueException(pricingModel, PricingModel.class);
        }
    }
}
