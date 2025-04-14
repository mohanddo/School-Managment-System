package com.mohand.SchoolManagmentSystem.response.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebhookResponse {
    private String id;
    private String entity;
    private String livemode;
    private String type;

    private CreateCheckoutResponse data;

    private Integer created_at;
    private Integer updated_at;
}
