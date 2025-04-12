package com.mohand.SchoolManagmentSystem.response.payment;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Map;

@Getter
@Setter
public class CreateProductResponse {
    private String id;
    private String entity;
    private String livemode;
    private String name;
    private String description;
    private String[] images;
//    private Map<String, Object> metadata;
    private Long created_at;
    private Long updated_at;
}
