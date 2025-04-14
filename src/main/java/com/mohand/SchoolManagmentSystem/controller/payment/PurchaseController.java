package com.mohand.SchoolManagmentSystem.controller.payment;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.response.payment.WebhookResponse;
import com.mohand.SchoolManagmentSystem.service.payment.IPurchaseService;
import com.mohand.SchoolManagmentSystem.service.payment.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/purchase")
public class PurchaseController {

    private final IPurchaseService purchaseService;

    @PostMapping("/cart")
    public ResponseEntity<String> purchaseCart(Authentication authentication) {

        Student student = (Student) authentication.getPrincipal();

        return ResponseEntity.ok(purchaseService.purchaseCart(student));
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<String> purchaseCourse(Authentication authentication, @PathVariable Long courseId) {

        Student student = (Student) authentication.getPrincipal();

        return ResponseEntity.ok(purchaseService.purchaseCourse(student, courseId));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleLiveEventsFromChargilyPay(HttpServletRequest request) {
        purchaseService.handleWebhook(request);
        return ResponseEntity.ok("Webhook received");
    }

}
