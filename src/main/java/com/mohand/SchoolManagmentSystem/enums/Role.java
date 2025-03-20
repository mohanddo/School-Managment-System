package com.mohand.SchoolManagmentSystem.enums;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_TEACHER("ROLE_TEACHER"),
    ROLE_STUDENT("ROLE_STUDENT");

    private final String value;

    Role(String value) {
        this.value = value;
    }

}
