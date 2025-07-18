package com.example.ecommerce.controllers;

import com.example.ecommerce.enums.Roles;
import com.example.ecommerce.model.Role;
import com.example.ecommerce.model.User;
import com.example.ecommerce.payload.LoginRequest;
import com.example.ecommerce.payload.UserInfoResponse;
import com.example.ecommerce.payload.MessageResponse;
import com.example.ecommerce.payload.SignUpRequest;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.jwt.JWTUtils;
import com.example.ecommerce.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private JWTUtils jwtUtils;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        }catch (AuthenticationException ex){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials!");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie responseCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserInfoResponse response = new UserInfoResponse(userDetails.getUsername(),roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest){
        if(userRepository.existsByUserName(signUpRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken"));
        }
        User user = new User(signUpRequest.getUsername(),
                            signUpRequest.getEmail(),
                            passwordEncoder.encode(signUpRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        Set<String> strRoles = signUpRequest.getRoles();

        if(strRoles == null || strRoles.isEmpty()){
            Role role = roleRepository.findByRoleName(Roles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role " + Roles.ROLE_USER + " not found"));
            roles.add(role);
        } else {
            strRoles.forEach(strRole -> {
                Role role = roleRepository.findByRoleName(Roles.valueOf(strRole))
                        .orElseThrow(() -> new RuntimeException("Role " +  strRole + " not found"));
                roles.add(role);
            });
        }
        user.setRoles(roles);

        userRepository.save(user);
        return ResponseEntity.ok("User Saved successfully");
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        ResponseCookie responseCookie = jwtUtils.generateCleanCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(new MessageResponse("Successfully logged out"));
    }
}
