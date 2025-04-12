package com.mohand.SchoolManagmentSystem.request.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class CreatePriceRequest {
    public CreatePriceRequest(Integer amount, String product_id) {
        this.amount = amount;
        this.product_id = product_id;
    }

    private Integer amount;
    private String currency = "dzd";
    private String product_id;
    private Map<String, Object> metadata = new HashMap<>();
}
