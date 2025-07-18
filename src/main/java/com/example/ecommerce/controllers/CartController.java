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
import com.example.ecommerce.payload.APIResponse;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    CartService cartService;
    CartMapper cartMapper;

    public CartController(CartService cartService, CartMapper cartMapper) {
        this.cartService = cartService;
        this.cartMapper = cartMapper;
    }

    @PostMapping("/products/{productId}/quantity/{quantity}")
    public ResponseEntity<APIResponse<CartDTO>> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return ResponseEntity.ok(new APIResponse<>(cartDTO, "Product added to cart", true));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<APIResponse<List<CartDTO>>> getAllCarts(){
        List<CartDTO> carts = cartService.getAllCarts();
        return ResponseEntity.ok(new APIResponse<>(carts, "All carts fetched", true));
    }

    @GetMapping("/users/cart")
    public ResponseEntity<APIResponse<CartDTO>> getCartById(){
        Cart cart = cartService.getOrCreateCart();
        return ResponseEntity.ok(new APIResponse<>(cartMapper.toCartDTO(cart), "User cart fetched", true));
    }

    @PutMapping("/products/{cartItemId}/quantity/{operation}")
    public ResponseEntity<APIResponse<CartDTO>> updateCartProductQuantity(@PathVariable Long cartItemId, @PathVariable String operation){
        CartDTO cartDTO = cartService.updateCartProductQuantity(cartItemId,
                operation.equalsIgnoreCase("add")? 1: -1);
        return ResponseEntity.ok(new APIResponse<>(cartDTO, "Cart product quantity updated", true));
    }

    @DeleteMapping("/product/{cartItemId}")
    public ResponseEntity<APIResponse<CartDTO>> deleteCartItem(@PathVariable Long cartItemId){
        CartDTO cartDTO = cartService.deleteCartItem(cartItemId);
        return ResponseEntity.ok(new APIResponse<>(cartDTO, "Cart item deleted", true));
    }
}
