package com.mohand.SchoolManagmentSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private Student student;

    @Value("${forgot.password.otp.expiration-time}")
    private Integer fpOTPExpirationTime;

    public ForgotPassword(Integer otp, Student student) {
        this.otp = otp;
        this.expirationTime = new Date(System.currentTimeMillis() + fpOTPExpirationTime);
        this.student = student;
    }

    public ForgotPassword(Integer otp, Date expirationTime, Student student) {
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.student = student;
    }
}
