package com.mohand.SchoolManagmentSystem.request.payment;

import com.mohand.SchoolManagmentSystem.enums.ChargilyPayFeesAllocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCheckoutRequest {
    private List<Item> items;
    private String success_url;
    private String failure_url;
    private ChargilyPayFeesAllocation chargily_pay_fees_allocation;
    private Integer amount_discount;
    private HashMap<String, Object> metadata;
}
