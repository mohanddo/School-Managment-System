package com.mohand.SchoolManagmentSystem.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohand.SchoolManagmentSystem.enums.Role;
import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.comment.UpVoteComment;
import com.mohand.SchoolManagmentSystem.model.course.AnnouncementComment;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.course.TeacherStudent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Teacher extends User {

    public Teacher(String firstName, String lastName, String email, String password, String verificationCode, LocalDateTime verificationCodeExpiresAt, String containerName) {
        super(firstName, lastName, email, password, verificationCode, verificationCodeExpiresAt, Role.ROLE_TEACHER);
        this.containerName = containerName;
        this.numberOfCourses = 0;
    }

    @Column(nullable = false)
    private Integer numberOfCourses;

    private String facebookLink;

    private String youtubeLink;

    private String instagramLink;

    private String description;

    private String sasToken;

    @Column(nullable = false, updatable = false)
    private String containerName;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TeacherStudent> students;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(Role.ROLE_TEACHER.getValue())
        );
    }

}
