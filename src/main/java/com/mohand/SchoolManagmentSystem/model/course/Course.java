package com.mohand.SchoolManagmentSystem.model.course;

import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import com.mohand.SchoolManagmentSystem.model.*;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder(builderMethodName = "hiddenBuilder")
@Check(constraints = "(discount_expiration_date IS NOT NULL OR (discount_percentage = 0 AND discount_expiration_date IS NULL))")
@Check(constraints = "(pricing_model = 'FREE' AND price = 0) OR (pricing_model <> 'FREE' AND price > 0)")
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
    private int numberOfReviews;

    @Column(nullable = false)
    private double numberOfHours;

    @Column(nullable = false)
    private long numberOfStudents;

    @Column(nullable = false)
    @Min(0)
    private double price;

    @Min(0)
    @Max(100)
    private int discountPercentage;

    private LocalDate discountExpirationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
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

    public static CourseBuilder builder(String title, String description, double price, PricingModel pricingModel, Teacher teacher, String imageUrl, String introductionVideoUrl) {
        return hiddenBuilder()
                .title(title)
                .description(description)
                .teacher(teacher)
                .numberOfStudents(0)
                .price(price)
                .pricingModel(pricingModel)
                .numberOfReviews(0)
                .numberOfHours(0)
                .imageUrl(imageUrl)
                .introductionVideoUrl(introductionVideoUrl)
                .discountPercentage(0);
    }
}
