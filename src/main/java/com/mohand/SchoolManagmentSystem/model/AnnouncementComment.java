package com.mohand.SchoolManagmentSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Check(constraints = "(teacher_id IS NOT NULL AND student_id IS NULL) OR (teacher_id IS NULL AND student_id IS NOT NULL)")
public class AnnouncementComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @AssertTrue(message = "A comment must have either a teacher or a student, but not both.")
    public boolean isTeacherOrStudent() {
        return (teacher != null && student == null) || (teacher == null && student != null);
    }

    @Column(nullable = false)
    private LocalDateTime dateOfCreation;

    @Column(nullable = false)
    private String text;
}
