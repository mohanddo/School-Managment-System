package com.mohand.SchoolManagmentSystem.model.comment;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"student_id", "reply_comment_id"}),
        @UniqueConstraint(columnNames = {"teacher_id", "reply_comment_id"})
})
@Check(constraints = "(teacher_id IS NOT NULL AND student_id IS NULL) OR (teacher_id IS NULL AND student_id IS NOT NULL)")
public class UpVoteReplyComment {
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
    @JoinColumn(name = "reply_comment_id")
    private ReplyComment replyComment;

    @AssertTrue(message = "An upvote must have either a teacher or a student, but not both.")
    public boolean isTeacherOrStudent() {
        return (teacher != null && student == null) || (teacher == null && student != null);
    }
}
