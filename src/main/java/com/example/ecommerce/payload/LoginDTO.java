package com.example.ecommerce.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String userId;

    @NotBlank
    private String userName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
