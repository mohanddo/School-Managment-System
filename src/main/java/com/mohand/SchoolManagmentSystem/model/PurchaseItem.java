package com.mohand.SchoolManagmentSystem.model;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "course_id", "purchase_id" }))
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    @Min(message = "Price must be greater than 0", value = 0)
    private int price;

    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private int discountPercentage;

    @Future(message = "Discount expiration date must be in the future")
    private LocalDate discountExpirationDate;

    @NotBlank
    @Column(nullable = false)
    private String priceId;

    public PurchaseItem(Course course, int price, int discountPercentage, LocalDate discountExpirationDate, String priceId, Purchase purchase) {
        this.course = course;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.discountExpirationDate = discountExpirationDate;
        this.priceId = priceId;
        this.purchase = purchase;
    }

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
}
