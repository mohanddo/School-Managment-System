package com.mohand.SchoolManagmentSystem.model.course;

import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.comment.UpVoteReplyComment;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"student_id", "teacher_id"}) })
public class TeacherStudent {

    public TeacherStudent(Student student, Teacher teacher) {
        this.student = student;
        this.teacher = teacher;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
