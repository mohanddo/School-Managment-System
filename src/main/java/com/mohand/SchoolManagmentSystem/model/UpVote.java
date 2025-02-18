package com.mohand.SchoolManagmentSystem.model;

import com.mohand.SchoolManagmentSystem.enums.Review;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "comment_id"}))
public class UpVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
