package com.mohand.SchoolManagmentSystem.model.course;

import com.mohand.SchoolManagmentSystem.model.chapter.Resource;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
@Getter
@NoArgsConstructor
public class CurrentResource {

    @EmbeddedId
    private CurrentResourceId id;

    public CurrentResource(Student student, Course course, Resource resource) {
        this.id = new CurrentResourceId();
        this.id.setStudentId(student.getId());
        this.id.setCourseId(course.getId());
        this.student = student;
        this.course = course;
        this.resource = resource;
    }

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

}
