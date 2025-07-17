package com.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {
//    private String jwtToken;
    private String username;
    private List<String> roles;
}
