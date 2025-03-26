package com.mohand.SchoolManagmentSystem.exception;

public class InvalidEnumValueException extends RuntimeException {
    public InvalidEnumValueException(String value, Class<? extends Enum<?>> enumClass) {
        super("Invalid value '" + value + "' for enum " + enumClass.getSimpleName());
    }
}
