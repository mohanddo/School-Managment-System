package com.mohand.SchoolManagmentSystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String token;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private LocalDateTime expiryDate;

    public PasswordResetToken(String token, Student student, LocalDateTime expiryDate) {
        this.token = token;
        this.student = student;
        this.expiryDate = expiryDate;
    }

    public boolean hasTokenExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }
}
