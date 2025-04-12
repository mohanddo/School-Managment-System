package com.mohand.SchoolManagmentSystem.request.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateCheckoutRequest {
    private String name;
    private String description;
    private String[] images;
}
