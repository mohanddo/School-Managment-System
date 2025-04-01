package com.mohand.SchoolManagmentSystem.model.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Announcement(String text, Course course) {
        this.text = text;
        this.course = course;
        this.dateOfCreation = LocalDateTime.now();
    }

    @NotBlank
    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime dateOfCreation;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnnouncementComment> announcementComments;
}
