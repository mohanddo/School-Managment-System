package com.mohand.SchoolManagmentSystem.model.course;

import com.mohand.SchoolManagmentSystem.enums.CourseCategory;
import com.mohand.SchoolManagmentSystem.enums.PricingModel;
import com.mohand.SchoolManagmentSystem.model.*;
import com.mohand.SchoolManagmentSystem.model.chapter.Chapter;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
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
@Check(constraints = "(discount_percentage > 0 OR (discount_percentage = 0 AND discount_expiration_date IS NULL))")
@Check(constraints = "(pricing_model = 'FREE' AND price = 0) OR (pricing_model <> 'FREE' AND price > 0)")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseCategory category;

    private String imageUrl;

    private String introductionVideoUrl;

    @Column(nullable = false)
    private Integer numberOfReviews;

    @Column(nullable = false)
    private Integer numberOfHours;

    @Column(nullable = false)
    private Integer numberOfStudents;

    @Column(nullable = false)
    @Min(message = "Price must be greater than 0", value = 0)
    private Integer price;

    @Min(0)
    @Max(100)
    private Integer discountPercentage;

    @Future(message = "Discount expiration date must be in the future")
    private LocalDate discountExpirationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PricingModel pricingModel;

    @NotBlank
    @Column(nullable = false)
    private String priceId;


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

    public static CourseBuilder builder(String title, String description, int price, PricingModel pricingModel, CourseCategory category, Teacher teacher, String imageUrl, String introductionVideoUrl) {
        return hiddenBuilder()
                .title(title)
                .description(description)
                .teacher(teacher)
                .numberOfStudents(0)
                .price(price)
                .pricingModel(pricingModel)
                .category(category)
                .numberOfReviews(0)
                .numberOfHours(0)
                .imageUrl(imageUrl)
                .introductionVideoUrl(introductionVideoUrl)
                .discountPercentage(0);

    }
}
