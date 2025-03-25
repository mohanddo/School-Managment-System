package com.mohand.SchoolManagmentSystem.enums;

import com.mohand.SchoolManagmentSystem.exception.pricingModel.InvalidPricingModelException;

public enum PricingModel {
    SUBSCRIPTION,
    ONE_TIME_PURCHASE,
    FREE;

    public static PricingModel validatePricingModel(String pricingModel) {
        try {
            return PricingModel.valueOf(pricingModel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidPricingModelException("The pricing model must be either SUBSCRIPTION, ONE_TIME_PURCHASE or FREE (not case sensitive)");
        }
    }
}
