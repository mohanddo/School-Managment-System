package com.mohand.SchoolManagmentSystem.model.chapter;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"student_id", "resource_id"}) } )
public class FinishedResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;
}
