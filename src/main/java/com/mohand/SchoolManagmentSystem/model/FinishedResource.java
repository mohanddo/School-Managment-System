package com.mohand.SchoolManagmentSystem.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
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
