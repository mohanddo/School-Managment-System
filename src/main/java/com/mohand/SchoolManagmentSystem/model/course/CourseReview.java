package com.mohand.SchoolManagmentSystem.model.course;

import com.mohand.SchoolManagmentSystem.enums.Review;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
@Builder(builderMethodName = "hiddenBuilder")
@Getter
@Setter
public class CourseReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Review review;

    private String comment;

    private LocalDateTime dateOfCreation;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public static CourseReviewBuilder builder(String comment, Review review, Student student, Course course) {
        return hiddenBuilder()
                .dateOfCreation(LocalDateTime.now())
                .comment(comment)
                .review(review)
                .student(student)
                .course(course);

    }
}
