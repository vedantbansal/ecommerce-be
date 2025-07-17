package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT c FROM CartItem c WHERE c.product.id = ?1 AND c.cart.id=?2")
    Optional<CartItem> findByProductIdAndCartId(Long productId, Long cartId);

    @Query("SELECT c FROM CartItem c WHERE c.id = ?1 AND c.cart.id=?2")
    Optional<CartItem> findByIdAndCartId(Long cartItemId, Long cartId);

    @Query("SELECT c FROM CartItem c WHERE c.product.id = ?1")
    List<CartItem> findByProductId(Long productId);
}
