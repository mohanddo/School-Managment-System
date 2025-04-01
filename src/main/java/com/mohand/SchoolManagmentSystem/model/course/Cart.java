package com.mohand.SchoolManagmentSystem.model.course;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"student_id", "course_id"}) } )
public class Cart {

    public Cart(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
