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

    public Chapter(String title, Course course, int position) {
        this.title = title;
        this.dateOfCreation = LocalDateTime.now();
        this.course = course;
        this.position = position;
    }

    @Column(nullable = false)
    private int position;


    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<Resource> resources;
}
