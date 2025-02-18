package com.mohand.SchoolManagmentSystem.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources;
}
