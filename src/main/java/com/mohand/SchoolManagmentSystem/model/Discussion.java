package com.mohand.SchoolManagmentSystem.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Entity
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "discussion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
