package com.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    @NotBlank(message = "Email is required")
    private String email;
    @NotNull(message = "Order items are required")
    private List<OrderItemDTO> orderItems;
    private LocalDate orderDate;
    private PaymentDTO payment;
    @NotNull(message = "Total amount is required")
    private Double totalAmount;
    @NotBlank(message = "Order status is required")
    private String orderStatus;
    private Long addressId;
}
