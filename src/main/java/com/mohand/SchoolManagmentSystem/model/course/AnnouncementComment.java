package com.mohand.SchoolManagmentSystem.model.course;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
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

    public AnnouncementComment(Student student, String text, Announcement announcement) {
        this.student = student;
        this.text = text;
        this.announcement = announcement;
        this.dateOfCreation = LocalDateTime.now();
    }

    public AnnouncementComment(Teacher teacher, Announcement announcement, String text) {
        this.teacher = teacher;
        this.announcement = announcement;
        this.text = text;
        this.dateOfCreation = LocalDateTime.now();
    }

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
