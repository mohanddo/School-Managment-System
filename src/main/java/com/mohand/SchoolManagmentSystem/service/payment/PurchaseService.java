package com.mohand.SchoolManagmentSystem.service.payment;

import com.mohand.SchoolManagmentSystem.model.course.CartItem;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService implements IPurchaseService {

    private final CartItemRepository cartItemRepository;

    @Override
    public void purchaseCart(User user) {
        List<CartItem> cartItems = cartItemRepository.findAllByStudentId(user.getId());
        for (CartItem cartItem : cartItems) {

        }
    }
}
