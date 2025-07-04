package com.mohand.SchoolManagmentSystem.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohand.SchoolManagmentSystem.enums.Role;
import com.mohand.SchoolManagmentSystem.model.*;
import com.mohand.SchoolManagmentSystem.model.chapter.FinishedResource;
import com.mohand.SchoolManagmentSystem.model.chapter.VideoProgress;
import com.mohand.SchoolManagmentSystem.model.course.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Student extends User {

    public Student(String firstName, String lastName, String email, String password, String verificationCode, LocalDateTime verificationCodeExpiresAt) {
        super(firstName, lastName, email, password, verificationCode, verificationCodeExpiresAt, Role.ROLE_STUDENT);
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"})
    )
    private List<Course> courses;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseReview> courseReviews;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinishedResource> finishedResources;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteCourse> favoriteCourses;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<Purchase> purchases;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TeacherStudent> teachers;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurrentResource> courseResources;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VideoProgress> videosProgress;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(Role.ROLE_STUDENT.getValue())
        );
    }
}
