package com.mohand.SchoolManagmentSystem.controller.chargilyPay;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("${api.prefix}/payments")
@RequiredArgsConstructor
public class ChargilyPayController {

    private final WebClient webClient;

    @GetMapping("/checkout")
    public ResponseEntity<?> createCheckout() {
        String response = webClient
                .post()
                .bodyValue("{\n  \"amount\": 2000,\n  \"currency\": \"dzd\",\n  \"success_url\": \"https://your-cool-website.com/payments/success\"\n}")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok().body(response);
    }

}
