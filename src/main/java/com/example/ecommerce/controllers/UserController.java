package com.example.ecommerce.controllers;

import com.example.ecommerce.payload.MessageResponse;
import com.example.ecommerce.payload.UserInfoResponse;
import com.example.ecommerce.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/username")
    public String getCurrentUsername(Authentication auth){
        if(auth != null && auth.isAuthenticated()){
            return auth.getName();
        }
        return "";
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication auth){
        if(auth != null && auth.isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getUsername(), roles);
            return ResponseEntity.ok().body(userInfoResponse);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Unauthorized user"));
        }
    }
}
