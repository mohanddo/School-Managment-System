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
//@AllArgsConstructor
public class CreateCheckoutRequest {
    private List<Item> items;
    private String success_url;
    private String failure_url;
    private ChargilyPayFeesAllocation chargily_pay_fees_allocation;
    private Integer amount_discount;
    private HashMap<String, Object> metadata;


    public CreateCheckoutRequest(List<Item> items, String success_url, String failure_url, ChargilyPayFeesAllocation chargily_pay_fees_allocation, Integer amount_discount, HashMap<String, Object> metadata) {
        this.metadata = metadata;
        this.amount_discount = amount_discount;
        this.chargily_pay_fees_allocation = chargily_pay_fees_allocation;
        this.failure_url = failure_url;
        this.success_url = success_url;
        this.items = items;
    }
}
