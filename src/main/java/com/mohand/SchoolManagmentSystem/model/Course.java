package com.mohand.SchoolManagmentSystem.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseReview> courseReviews;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters;
}
