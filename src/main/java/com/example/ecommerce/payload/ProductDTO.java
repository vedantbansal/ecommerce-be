package com.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    @NotBlank(message = "Product name is required")
    private String productName;
    private String image;
    @NotBlank(message = "Description is required")
    private String description;
    private String category;
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    @NotNull(message = "Price is required")
    private double price;
    private double discount;
    private double specialPrice;
}

