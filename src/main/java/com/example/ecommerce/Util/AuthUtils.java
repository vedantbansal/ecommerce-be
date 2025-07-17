package com.example.ecommerce.Util;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    @Autowired
    UserRepository userRepository;

    public String getLoggedInUserEmail(){
        return getLoggedInUser().getEmail();
    }

    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));
    }

}
