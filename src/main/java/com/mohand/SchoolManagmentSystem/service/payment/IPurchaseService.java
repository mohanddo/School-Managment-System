package com.mohand.SchoolManagmentSystem.service.payment;

import com.mohand.SchoolManagmentSystem.model.user.Student;

public interface IPurchaseService {

    String purchaseCart(Student student);
    String purchaseCourse(Student student, Long courseId);
}
