package com.mohand.SchoolManagmentSystem.model;

import com.mohand.SchoolManagmentSystem.response.payment.CreateCheckoutResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ProcessedWebhooks {
    @Id
    private String id;

    public ProcessedWebhooks(String id, String entity, String livemode, String type, Integer created_at, Integer updated_at) {
        this.id = id;
        this.entity = entity;
        this.livemode = livemode;
        this.type = type;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @NotBlank
    @Column(nullable = false)
    private String entity;

    @NotBlank
    @Column(nullable = false)
    private String livemode;

    @NotBlank
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer created_at;

    @Column(nullable = false)
    private Integer updated_at;
}
