package com.mohand.SchoolManagmentSystem.request.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class CreateProductRequest {
    private String name;
    private String description;
    private String[] images;
    private Map<String, Object> metadata = new HashMap<>();
}
