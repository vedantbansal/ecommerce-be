package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.payload.CartDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
    List<CartDTO> getAllCarts();
    Cart getOrCreateCart();
    @Transactional
    CartDTO updateCartProductQuantity(Long cartItemId, Integer quantity);
    CartDTO deleteCartItem(Long cartItemId);
}
