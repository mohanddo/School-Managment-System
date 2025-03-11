package com.mohand.SchoolManagmentSystem.controller;

import com.mohand.SchoolManagmentSystem.exception.student.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.student.account.AccountException;
import com.mohand.SchoolManagmentSystem.exception.student.account.AccountNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.student.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.student.password.ChangePasswordException;
import com.mohand.SchoolManagmentSystem.exception.student.password.PasswordException;
import com.mohand.SchoolManagmentSystem.exception.student.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.student.password.WrongPasswordException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.StudentAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.VerificationCodeException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.VerificationCodeInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VerificationCodeException.class)
    public ProblemDetail handleVerificationCodeException(VerificationCodeException exception) {

        ProblemDetail errorDetail = null;

        exception.printStackTrace();

        if (exception instanceof StudentAlreadyVerifiedException || exception instanceof VerificationCodeInvalidException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        }

        if (exception instanceof VerificationCodeExpiredException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.GONE, exception.getMessage());
        }

        return errorDetail;
    }

    @ExceptionHandler(AccountException.class)
    public ProblemDetail handleAccountException(AccountException exception) {

        ProblemDetail errorDetail = null;

        exception.printStackTrace();

        if (exception instanceof AccountNotFoundException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        }

        if (exception instanceof AccountAlreadyExistException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        }

        if (exception instanceof AccountNotEnabledException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
        }

        return errorDetail;
    }

    @ExceptionHandler(PasswordException.class)
    public ProblemDetail handlePasswordException(PasswordException exception) {

        ProblemDetail errorDetail = null;

        exception.printStackTrace();

        if (exception instanceof WrongPasswordException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        }

        if (exception instanceof ChangePasswordException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        }

        if (exception instanceof WeakPasswordException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        }

        return errorDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception exception) {
        exception.printStackTrace();

        if (exception instanceof BadCredentialsException) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Incorrect email or password.");
        }

        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
    }
}
