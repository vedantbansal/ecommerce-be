package com.example.ecommerce.security.jwt;

import com.example.ecommerce.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.app.jwtSecretKey}")
    private String jwtSecretKey;

    @Value("${spring.app.jwtCookie}")
    private String jwtCookie;

    @Value("${spring.app.cookieExpSec}")
    private Long cookieExpSec;

    @Value("${spring.app.secureCookie}")
    private Boolean secureCookie;
//    public String getJWTFromHeader(HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

    public ResponseCookie generateCleanCookie(){
        return ResponseCookie.from(jwtCookie)
                .path("/api")
                .build();
    }

    public String getJWTFromCookie(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails){
        String jwt = generateTokenFromUsername(userDetails);
        return  ResponseCookie.from(jwtCookie, jwt)
                .path("/api")
                .maxAge(cookieExpSec)
                .secure(secureCookie)
                .build();
    }

    public String generateTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() +jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String getUsernameFromJWTToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJWTToken(String authToken){
        try{
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
        } catch (ExpiredJwtException e){
        } catch (UnsupportedJwtException e){
        } catch (IllegalArgumentException e){
        }
        return false;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }
}
