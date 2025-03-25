package com.mohand.SchoolManagmentSystem.controller;

import com.mohand.SchoolManagmentSystem.exception.course.CourseException;
import com.mohand.SchoolManagmentSystem.exception.course.CourseNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.pricingModel.InvalidPricingModelException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.user.password.ChangePasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.password.PasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WrongPasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.AccountAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeInvalidException;
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

        if (exception instanceof AccountAlreadyVerifiedException || exception instanceof VerificationCodeInvalidException) {
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

    @ExceptionHandler(CourseException.class)
    public ProblemDetail handleCourseException(CourseException exception) {
        exception.printStackTrace();
        ProblemDetail errorDetail = null;

        if (exception instanceof CourseNotFoundException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        }

        return errorDetail;
    }

    @ExceptionHandler(InvalidPricingModelException.class)
    public ProblemDetail handleInvalidPricingModelException(InvalidPricingModelException exception) {
        exception.printStackTrace();
        ProblemDetail errorDetail = null;

        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());

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
