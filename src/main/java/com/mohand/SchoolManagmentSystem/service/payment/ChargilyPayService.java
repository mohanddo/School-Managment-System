package com.mohand.SchoolManagmentSystem.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohand.SchoolManagmentSystem.model.course.CartItem;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.request.payment.CreatePriceRequest;
import com.mohand.SchoolManagmentSystem.request.payment.CreateProductRequest;
import com.mohand.SchoolManagmentSystem.response.payment.CreatePriceResponse;
import com.mohand.SchoolManagmentSystem.response.payment.CreateProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargilyPayService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Value("${chargily.pay.secret.key}")
    private String chargilyPaySecretKey;

    @Value("${chargily.pay.base.url}")
    private String chargilyPayBaseUrl;

//    void createCheckout(List<CartItem> cartItems) {
//
//        try {
//            for(CartItem cartItem : cartItems) {
//                CreateProductResponse createProductResponse = createProduct(cartItem);
//                createPrice(createProductResponse.getId(), cartItem.getCourse().getPrice());
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void createPrice(String productId, Integer amount) throws IOException, InterruptedException {
        CreatePriceRequest price = new CreatePriceRequest(amount, productId);
        String createPriceRequestBody = objectMapper.writeValueAsString(price);

        HttpRequest createPrice = HttpRequest.newBuilder()
                .uri(URI.create(chargilyPayBaseUrl + "/prices"))
                .POST(HttpRequest.BodyPublishers.ofString(createPriceRequestBody))
                .header("Authorization", "Bearer " + chargilyPaySecretKey)
                .header("Content-Type", "application/json")
                .build();

       HttpResponse<String> createPriceResponseBody = client.send(createPrice, HttpResponse.BodyHandlers.ofString());

        if (createPriceResponseBody.statusCode() != 200) {
            throw new RuntimeException("Failed to create price");
        }

//        return objectMapper.readValue(
//                createPriceResponseBody.body(), CreatePriceResponse.class
//        );
    }

    public void createProduct(Course course)  {
        try {
            CreateProductRequest product = modelMapper.map(course, CreateProductRequest.class);
            String createProductRequestBody = objectMapper.writeValueAsString(product);
            System.out.println(createProductRequestBody);

            HttpRequest createProduct = HttpRequest.newBuilder()
                    .uri(URI.create(chargilyPayBaseUrl + "/products"))
                    .POST(HttpRequest.BodyPublishers.ofString(createProductRequestBody))
                    .header("Authorization", "Bearer " + chargilyPaySecretKey)
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> createProductResponseBody = client.send(createProduct, HttpResponse.BodyHandlers.ofString());
            System.out.println(createProductResponseBody.body());
            System.out.println(createProductResponseBody.statusCode());
            System.out.println(createProductResponseBody.headers());
            if (createProductResponseBody.statusCode() == 200) {
                CreateProductResponse createProductResponse = objectMapper.readValue(
                        createProductResponseBody.body(), CreateProductResponse.class
                );
                createPrice(createProductResponse.getId(), course.getPrice());
            } else {
                throw new RuntimeException("Failed to create product");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create product");
        }
    }

}
