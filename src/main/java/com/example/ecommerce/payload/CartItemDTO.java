package com.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO  {
    private Long cartItemId;
    private ProductDTO product;
    private Integer quantity = 0;
}