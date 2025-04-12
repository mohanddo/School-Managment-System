package com.mohand.SchoolManagmentSystem.response.payment;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CreatePriceResponse {
    private String id;
    private String product_id;
    private String entity;
    private String livemode;
    private Integer amount;
    private String currency;
    private Map<String, Object> metadata;
    private Long created_at;
    private Long updated_at;
}
