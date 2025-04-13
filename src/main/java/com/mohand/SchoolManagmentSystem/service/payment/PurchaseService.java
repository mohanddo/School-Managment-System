package com.mohand.SchoolManagmentSystem.service.payment;

import com.mohand.SchoolManagmentSystem.enums.ChargilyPayFeesAllocation;
import com.mohand.SchoolManagmentSystem.exception.BadRequestException;
import com.mohand.SchoolManagmentSystem.model.course.CartItem;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.CartItemRepository;
import com.mohand.SchoolManagmentSystem.request.payment.CreateCheckoutRequest;
import com.mohand.SchoolManagmentSystem.request.payment.Item;
import com.mohand.SchoolManagmentSystem.service.course.CourseService;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService implements IPurchaseService {

    private final CartItemRepository cartItemRepository;
    private final ChargilyPayService chargilyPayService;
    private final ICourseService courseService;

    @Value("${chargily.pay.success.url}")
    private String successUrl;

    @Value("${chargily.pay.failure.url}")
    private String failureUrl;

    @Override
    public String purchaseCart(Student student) {
        List<CartItem> cartItems = cartItemRepository.findAllByStudentId(student.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Your cart is empty");
        }

        List<Item> items = new ArrayList<>();
        int amount_discount = 0;

        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("user_id", student.getId().toString());
        StringBuilder course_Ids = new StringBuilder();

        for (CartItem cartItem : cartItems) {
            Course course = cartItem.getCourse();
            amount_discount += (course.getPrice() * course.getDiscountPercentage() / 100);
            items.add(new Item(course.getPriceId(), 1));
            course_Ids.append(course.getId()).append(",");
        }

        metadata.put("course_ids", course_Ids.toString());


        CreateCheckoutRequest createCheckoutRequest =
                new CreateCheckoutRequest(items, successUrl, failureUrl, ChargilyPayFeesAllocation.customer, amount_discount, metadata);

        return chargilyPayService.createCheckout(createCheckoutRequest);
    }

    @Override
    public String purchaseCourse(Student student, Long courseId) {
        Course course = courseService.getById(courseId);

        List<Item> items = new ArrayList<>();
        items.add(new Item(course.getPriceId(), 1));

        int amount_discount = course.getPrice() - (course.getPrice() * course.getDiscountPercentage() / 100);

        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("user_id", student.getId().toString());
        metadata.put("course_ids", course.getId().toString());

        CreateCheckoutRequest createCheckoutRequest =
                new CreateCheckoutRequest(items, successUrl, failureUrl, ChargilyPayFeesAllocation.customer, amount_discount, metadata);

        return chargilyPayService.createCheckout(createCheckoutRequest);
    }
}
