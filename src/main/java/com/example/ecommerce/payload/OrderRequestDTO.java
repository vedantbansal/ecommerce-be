package com.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @NotNull(message = "Address ID is required")
    private Long addressId;
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    @NotBlank(message = "PG name is required")
    private String pgName;
    @NotBlank(message = "PG payment ID is required")
    private String pgPaymentId;
    @NotBlank(message = "PG status is required")
    private String pgStatus;
    @NotBlank(message = "PG response message is required")
    private String pgResponseMessage;
}
