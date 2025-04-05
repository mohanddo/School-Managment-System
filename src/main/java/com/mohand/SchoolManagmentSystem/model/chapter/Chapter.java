package com.mohand.SchoolManagmentSystem.model.chapter;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime dateOfCreation;

    public Chapter(String title, Course course) {
        this.title = title;
        this.dateOfCreation = LocalDateTime.now();
        this.course = course;
    }

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources;
}
