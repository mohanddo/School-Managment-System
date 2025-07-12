package com.mohand.SchoolManagmentSystem.model;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private LocalDateTime dateOfCreation;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Purchase(Double amount, LocalDateTime dateOfCreation, Student student) {
        this.amount = amount;
        this.dateOfCreation = dateOfCreation;
        this.student = student;
    }

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> purchaseItems;
}
