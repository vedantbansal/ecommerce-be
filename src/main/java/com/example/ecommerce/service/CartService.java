package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.payload.CartDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartService {
    ResponseEntity<CartDTO> addProductToCart(Long productId, Integer quantity);
    ResponseEntity<List<CartDTO>> getAllCarts();
    Cart getOrCreateCart();
    @Transactional
    ResponseEntity<CartDTO> updateCartProductQuantity(Long cartItemId, Integer quantity);
    ResponseEntity<CartDTO> deleteCartItem(Long cartItemId);
}
