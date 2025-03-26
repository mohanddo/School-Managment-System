package com.mohand.SchoolManagmentSystem.model.comment;

import com.mohand.SchoolManagmentSystem.model.Resource;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Entity
@Check(constraints = "(teacher_id IS NOT NULL AND student_id IS NULL) OR (teacher_id IS NULL AND student_id IS NOT NULL)")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private LocalDate dateOfCreation;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpVoteComment> upVoteComments;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplyComment> replyComments;

    @AssertTrue(message = "A comment must have either a teacher or a student, but not both.")
    public boolean isTeacherOrStudent() {
        return (teacher != null && student == null) || (teacher == null && student != null);
    }
}
