package com.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MessageResponse {
    private String message;
    public MessageResponse(String msg) {
        this.message = msg;
    }
}
