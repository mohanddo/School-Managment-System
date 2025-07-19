package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.User;
import com.mohand.SchoolManagmentSystem.service.authentication.AuthenticationService;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("${api.prefix}/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;

    @Value("${azure.storage.endpoint}")
    private String azureStorageEndpoint;

    @Value("${same.site}")
    private String sameSite;

    @Value("${domain.name}")
    private String domainName;

    public AuthenticationController(AuthenticationService authenticationService, ModelMapper modelMapper, AzureBlobService azureBlobService){
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
    }


    @Value("${send.cookie.over.https}")
    private String sendCookieOverHttps;

    @PostMapping("/logout")
    private void logout(HttpServletResponse response) {
        String jwtCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(Boolean.parseBoolean(sendCookieOverHttps))
                .sameSite(sameSite)
                .path("/")
                .maxAge(0)
                .domain(domainName)
                .build().toString();

        String isLoggedCookie = ResponseCookie.from("isLogged", "")
                .httpOnly(false)
                .secure(Boolean.parseBoolean(sendCookieOverHttps))
                .sameSite(sameSite)
                .path("/")
                .maxAge(0)
                .domain(domainName)
                .build().toString();

        response.addHeader("Set-Cookie", isLoggedCookie);
        response.addHeader("Set-Cookie", jwtCookie);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LogInUserRequest request, HttpServletResponse response) {
        authenticationService.authenticate(request, response);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendEmail(@RequestParam String email) {
        authenticationService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification email resent successfully");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyStudent(@Valid @RequestBody VerifyUserRequest request, HttpServletResponse response) {
        authenticationService.verifyUser(request, response);
        return ResponseEntity.ok("Verification successful");
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(Authentication authentication) {
        com.mohand.SchoolManagmentSystem.model.user.User user = (com.mohand.SchoolManagmentSystem.model.user.User) authentication.getPrincipal();
        User userResponse = modelMapper.map(user, User.class);
        userResponse.setSasTokenForReadingProfilePic(azureBlobService.generateSasTokenForBlob(azureStorageEndpoint + "/profilepics/" + user.getId()));
        userResponse.setSasTokenForWritingProfilePic(azureBlobService.generateSasTokenForBlob(azureStorageEndpoint + "/profilepics/" + user.getId(), true));
        return ResponseEntity.ok(userResponse);
    }
}

