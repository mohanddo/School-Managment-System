package com.mohand.SchoolManagmentSystem.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohand.SchoolManagmentSystem.enums.ChargilyPayFeesAllocation;
import com.mohand.SchoolManagmentSystem.exception.BadRequestException;
import com.mohand.SchoolManagmentSystem.exception.ConflictException;
import com.mohand.SchoolManagmentSystem.model.ProcessedWebhooks;
import com.mohand.SchoolManagmentSystem.model.Purchase;
import com.mohand.SchoolManagmentSystem.model.PurchaseItem;
import com.mohand.SchoolManagmentSystem.model.course.CartItem;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.repository.CartItemRepository;
import com.mohand.SchoolManagmentSystem.repository.ProcessedWebhooksRepository;
import com.mohand.SchoolManagmentSystem.repository.PurchaseItemRepository;
import com.mohand.SchoolManagmentSystem.repository.PurchaseRepository;
import com.mohand.SchoolManagmentSystem.request.payment.CreateCheckoutRequest;
import com.mohand.SchoolManagmentSystem.request.payment.Item;
import com.mohand.SchoolManagmentSystem.response.payment.CreateCheckoutResponse;
import com.mohand.SchoolManagmentSystem.response.payment.WebhookResponse;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import com.mohand.SchoolManagmentSystem.service.student.IStudentService;
import com.mohand.SchoolManagmentSystem.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService implements IPurchaseService {

    private final CartItemRepository cartItemRepository;
    private final ChargilyPayService chargilyPayService;
    private final IStudentService studentService;
    private final ICourseService courseService;
    private final ProcessedWebhooksRepository processedWebhooksRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ObjectMapper objectMapper;

    @Value("${chargily.pay.success.url}")
    private String successUrl;

    @Value("${chargily.pay.failure.url}")
    private String failureUrl;

    @Override
    @Transactional
    public String purchaseCart(Student student) {
        List<CartItem> cartItems = cartItemRepository.findAllByStudentId(student.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Your cart is empty");
        }

        List<Item> items = new ArrayList<>();
        int amount_discount = 0;
        double amount = 0;

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("user_id", student.getId());
        List<Long> course_Ids = new ArrayList<>();
        List<Course> courses = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Course course = cartItem.getCourse();
            amount += course.getPrice();
            amount_discount += (course.getPrice() * course.getDiscountPercentage() / 100);
            items.add(new Item(course.getPriceId(), 1));
            course_Ids.add(course.getId());
            courses.add(course);
        }

        List<PurchaseItem> purchaseItems = new ArrayList<>();
        if (amount - amount_discount <= 50) {
            Purchase purchase = new Purchase(amount, LocalDateTime.now(), student);
            purchase = purchaseRepository.save(purchase);

            for (Course course: courses) {
                studentService.addStudentToCourse(course.getId(), student.getId());
                PurchaseItem purchaseItem = new PurchaseItem(course, course.getPrice(),
                        course.getDiscountPercentage(),
                        course.getDiscountExpirationDate(),
                        course.getPriceId(),
                        purchase);
                purchaseItems.add(purchaseItem);
            }
            purchaseItemRepository.saveAll(purchaseItems);
            return null;
        }

        metadata.put("course_ids", course_Ids);

        CreateCheckoutRequest createCheckoutRequest =
                new CreateCheckoutRequest(items, successUrl, failureUrl, ChargilyPayFeesAllocation.customer, amount_discount, metadata);

        return chargilyPayService.createCheckout(createCheckoutRequest);
    }

    @Override
    public String purchaseCourse(Student student, Long courseId) {

        if (courseService.existsByIdAndStudentId(courseId, student.getId())) {
            throw new ConflictException("Student already enrolled in this course");
        }

        Course course = courseService.getById(courseId);

        int amount_discount = (course.getPrice() * course.getDiscountPercentage() / 100);
        double amount = course.getPrice() - amount_discount;
        if (amount <= 50) {
            Purchase purchase = new Purchase(amount, LocalDateTime.now(), student);
            purchase = purchaseRepository.save(purchase);
            PurchaseItem purchaseItem = new PurchaseItem(course, course.getPrice(),
                    course.getDiscountPercentage(),
                    course.getDiscountExpirationDate(),
                    course.getPriceId(),
                    purchase);
            purchaseItemRepository.save(purchaseItem);
            studentService.addStudentToCourse(Long.valueOf(courseId), student.getId());
            return null;
        }

        List<Item> items = new ArrayList<>();
        items.add(new Item(course.getPriceId(), 1));

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("user_id", student.getId());
        List<Long> course_Ids = List.of(courseId);
        metadata.put("course_ids", course_Ids);

        CreateCheckoutRequest createCheckoutRequest =
                new CreateCheckoutRequest(items, successUrl, failureUrl, ChargilyPayFeesAllocation.customer, amount_discount, metadata);

        return chargilyPayService.createCheckout(createCheckoutRequest);
    }

    @Override
    @Transactional
    public void handleWebhook(HttpServletRequest req) {

        String requestBody = Util.getRequestBody(req);
        String signature = req.getHeader("signature");

        WebhookResponse webhookResponse;
        try {
            webhookResponse = objectMapper.readValue(
                    requestBody, WebhookResponse.class
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        if (!chargilyPayService.isSignatureValid(requestBody, signature)) {
            return;
        }

            CreateCheckoutResponse payload = webhookResponse.getData();
            if (!payload.getEntity().equals("checkout") || !payload.getStatus().equals("paid")) {
                return;
            }

            if (processedWebhooksRepository.existsById(webhookResponse.getId())) {
                return;
            }

            Long studentId = Long.valueOf( (Integer) payload.getMetadata().get("user_id"));
            Student student = studentService.getById(studentId);
            Purchase purchase = new Purchase(payload.getAmount(),  LocalDateTime.ofInstant(Instant.ofEpochSecond(payload.getCreated_at()), ZoneId.systemDefault()), student);
            purchase = purchaseRepository.save(purchase);

            List<PurchaseItem> purchaseItems = new ArrayList<>();
            List<Integer> courseIds = (List<Integer>) payload.getMetadata().get("course_ids");
            for (Integer courseId : courseIds) {
                studentService.addStudentToCourse(Long.valueOf(courseId), studentId);
                Course course = courseService.getById(Long.valueOf(courseId));
                PurchaseItem purchaseItem = new PurchaseItem(course, course.getPrice(),
                        course.getDiscountPercentage(),
                        course.getDiscountExpirationDate(),
                        course.getPriceId(),
                        purchase);
                purchaseItems.add(purchaseItem);
            }
            purchaseItemRepository.saveAll(purchaseItems);


            ProcessedWebhooks processedWebhooks = new ProcessedWebhooks(webhookResponse.getId(), webhookResponse.getEntity(), webhookResponse.getLivemode(), webhookResponse.getType(), webhookResponse.getCreated_at(), webhookResponse.getUpdated_at());
            processedWebhooksRepository.save(processedWebhooks);
    }
}


