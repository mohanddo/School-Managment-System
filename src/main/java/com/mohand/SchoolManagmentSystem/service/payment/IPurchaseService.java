package com.mohand.SchoolManagmentSystem.service.payment;

import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.response.payment.WebhookResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IPurchaseService {

    String purchaseCart(Student student);
    String purchaseCourse(Student student, Long courseId);
    void handleWebhook(HttpServletRequest request);
}
