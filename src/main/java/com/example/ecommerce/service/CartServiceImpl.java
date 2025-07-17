package com.example.ecommerce.service;

import com.example.ecommerce.Util.AuthUtils;
import com.example.ecommerce.exceptions.ApiException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mappers.CartMapper;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.CartDTO;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService{

    CartRepository cartRepository;
    ProductRepository productRepository;
    CartItemRepository cartItemRepository;
    AuthUtils authUtils;
    CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository,
                           ProductRepository productRepository,
                           CartItemRepository cartItemRepository,
                           AuthUtils authUtils,
                           CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.authUtils = authUtils;
        this.cartMapper=cartMapper;
    }

    @Override
    @Transactional
    public ResponseEntity<CartDTO> addProductToCart(Long productId, Integer quantity) {
        if(quantity <= 0){
            throw new ApiException("Quantity in invalid for product " + productId);
        }
        Cart cart = getOrCreateCart();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Product not found with ID: " + productId));
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getCartId())
                .orElseGet(() -> {
                   CartItem newCartItem = new CartItem();
                   newCartItem.setCart(cart);
                   newCartItem.setProduct(product);
                   return newCartItem;
                });
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        if(product.getQuantity() < quantity){
            throw new ApiException("Only " + product.getQuantity() + " items left in stock for item " + product.getProductName());
        }
        List<CartItem> cartItemList = cart.getCartItems();
        cart.getCartItems().add(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() + product.getSpecialPrice() * quantity);

        Cart savedCart = cartRepository.save(cart);
        CartDTO cartDTO = cartMapper.toCartDTO(savedCart);

        return ResponseEntity.ok(cartDTO);
    }

    @Override
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return ResponseEntity.ok(carts.stream().map(item -> cartMapper.toCartDTO(item)).toList());
    }

    @Override
    public Cart getOrCreateCart(){
        return cartRepository.findByEmail(authUtils.getLoggedInUserEmail())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(authUtils.getLoggedInUser());
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    @Override
    public ResponseEntity<CartDTO> updateCartProductQuantity(Long cartItemId, Integer quantity) {
        Cart cart = getOrCreateCart();
        CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getCartId())
                .orElseThrow(() -> new ApiException("Cart Item not found with Id: " + cartItemId));

        Integer newQuantity = cartItem.getQuantity() + quantity;
        if(newQuantity == 0){
            cart.getCartItems().removeIf(item -> Objects.equals(item.getCartItemId(), cartItemId));
        } else {
            if (newQuantity > cartItem.getProduct().getQuantity()) {
                throw new ApiException("Only " + cartItem.getQuantity() + " items left in stock for item " + cartItem.getProduct().getProductName());
            }
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
        cart.setTotalPrice(cart.getTotalPrice() + quantity * cartItem.getProduct().getSpecialPrice());
        Cart updatedCart = cartRepository.save(cart);
        CartDTO cartDTO = cartMapper.toCartDTO(updatedCart);
        return ResponseEntity.ok(cartDTO);
    }

    @Transactional
    @Override
    public ResponseEntity<CartDTO> deleteCartItem(Long cartItemId) {
        Cart cart = getOrCreateCart();
        CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "CartItemId", cartItemId));
        cart.getCartItems().removeIf(item -> Objects.equals(item.getCartItemId(), cartItemId));
        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getQuantity() * cartItem.getProduct().getSpecialPrice());
        Cart savedCart = cartRepository.save(cart);
        CartDTO cartDTO = cartMapper.toCartDTO(savedCart);
        return ResponseEntity.ok(cartDTO);
    }


}
