package com.mohand.SchoolManagmentSystem.response.payment;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class CreateCheckoutResponse {
    private String id;
    private String entity;
    private Integer fees;
    private Integer amount;
    private String local;
    private String status;
    private HashMap<String, Object> metadata;
    private String invoice_id;
    private String customer_id;
    private String description;
    private String success_url;
    private String failure_url;

    private String payment_method;
    private String payment_link_id;
    private String chargily_pay_fees_allocation;

    private String amount_without_discount;
    private String url;

    private Integer created_at;
    private Integer updated_at;
}
