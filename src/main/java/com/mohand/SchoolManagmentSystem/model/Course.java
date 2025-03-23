package com.mohand.SchoolManagmentSystem.model;

import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder(builderMethodName = "hiddenBuilder")
@Check(constraints = "(discount_percentage IS NOT NULL AND discount_expiration_date IS NOT NULL) OR (discount_percentage IS NULL AND discount_expiration_date IS NULL)")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String description;

    private String imageUrl;

    private String introductionVideoUrl;

    @Column(nullable = false)
    private long numberOfStudents;

    @Column(nullable = false)
    private double price;

    @Min(1)
    @Max(100)
    private int discountPercentage;

    private LocalDate discountExpirationDate;

    @Column(nullable = false)
    private PricingModel pricingModel;


    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseReview> courseReviews;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Announcement> announcements;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteCourse> favoriteCourses;

    @OneToMany(mappedBy = "course")
    private List<OrderItem> orderItems;

    public static CourseBuilder builder(String title, String description, double price, PricingModel pricingModel, Teacher teacher) {
        return hiddenBuilder()
                .title(title)
                .description(description)
                .teacher(teacher)
                .numberOfStudents(0)
                .price(price)
                .pricingModel(pricingModel);
    }
}
