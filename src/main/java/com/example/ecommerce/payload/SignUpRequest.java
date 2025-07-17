package com.example.ecommerce.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    @Size(min=3, max=20)
    private String username;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min=8)
    private String password;
    private Set<String> roles;
}
