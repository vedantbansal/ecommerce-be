package com.example.ecommerce.controllers;

import com.example.ecommerce.mappers.CartMapper;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.payload.CartDTO;
import com.example.ecommerce.payload.CartItemDTO;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    CartMapper cartMapper;

    @PostMapping("/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        return cartService.addProductToCart(productId, quantity);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts(){
        return cartService.getAllCarts();
    }

    @GetMapping("/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        Cart cart =cartService.getOrCreateCart();
        return new ResponseEntity<CartDTO>(cartMapper.toCartDTO(cart),HttpStatus.OK);
    }

    @PutMapping("/products/{cartItemId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProductQuantity(@PathVariable Long cartItemId, @PathVariable String operation){
        return cartService.updateCartProductQuantity(cartItemId,
                operation.equalsIgnoreCase("add")? 1: -1);
    }

    @DeleteMapping("/product/{cartItemId}")
    public ResponseEntity<CartDTO> deleteCartItem(@PathVariable Long cartItemId){
        return cartService.deleteCartItem(cartItemId);
    }
}
