package com.mohand.SchoolManagmentSystem.controller.payment;

import com.mohand.SchoolManagmentSystem.service.payment.IPurchaseService;
import com.mohand.SchoolManagmentSystem.service.payment.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/purchase")
public class PurchaseController {

    private final IPurchaseService purchaseService;

    @PostMapping("/cart")
    public ResponseEntity purchaseCart() {
        return ResponseEntity.ok().build();
    }

}
