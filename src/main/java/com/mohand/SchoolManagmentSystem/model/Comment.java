package com.mohand.SchoolManagmentSystem.model;

import com.mohand.SchoolManagmentSystem.enums.Review;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Entity
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
    @JoinColumn(name = "discussion_id")
    private Discussion discussion;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpVote> upVotes;
}
