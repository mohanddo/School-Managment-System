package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.ForgotPassword;
import com.mohand.SchoolManagmentSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    Optional<ForgotPassword> findForgotPasswordByOtpAndStudent(Integer otp, Student student);
}
