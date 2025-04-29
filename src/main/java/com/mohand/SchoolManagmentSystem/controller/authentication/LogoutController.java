package com.mohand.SchoolManagmentSystem.controller.authentication;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${api.prefix}/logout")
@RequiredArgsConstructor
public class LogoutController {

    @Value("${send.cookie.over.https}")
    private String sendCookieOverHttps;

    @PostMapping
    private void logout(HttpServletResponse response) {
        String jwtCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(Boolean.parseBoolean(sendCookieOverHttps))
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build().toString();

        String isLoggedCookie = ResponseCookie.from("isLogged", "")
                .httpOnly(false)
                .secure(Boolean.parseBoolean(sendCookieOverHttps))
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build().toString();

        response.addHeader("Set-Cookie", isLoggedCookie);
        response.addHeader("Set-Cookie", jwtCookie);
    }
}


