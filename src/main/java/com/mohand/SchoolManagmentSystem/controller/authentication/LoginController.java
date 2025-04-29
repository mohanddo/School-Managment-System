package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.Teacher;
import com.mohand.SchoolManagmentSystem.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${api.prefix}/login")
public class LoginController {
    private final AuthenticationService authenticationService;

   public LoginController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LogInUserRequest request, HttpServletResponse response) {
        authenticationService.authenticate(request, response);
        return ResponseEntity.ok("Login successful");
    }

}
