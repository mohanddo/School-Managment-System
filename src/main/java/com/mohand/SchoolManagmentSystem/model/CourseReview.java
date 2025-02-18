package com.mohand.SchoolManagmentSystem.model;

import com.mohand.SchoolManagmentSystem.enums.Review;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
public class CourseReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Review review;
    private String comment;
    private LocalDate dateOfCreation;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
