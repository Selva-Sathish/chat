package com.example.chatApp.controller;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.example.chatApp.dto.LoginRequest;
import com.example.chatApp.dto.TokenResponse;
import com.example.chatApp.security.UserDetailService;

import com.example.chatApp.utils.JwtUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserDetailService userDetailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(UserDetailService userDetailService,
             AuthenticationManager authenticationManager,
             JwtUtils jwtUtils
        ){
        this.userDetailService = userDetailService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authencation(@RequestBody LoginRequest request){
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        String accessToken = jwtUtils.generateToken(request.getUsername(), 15 * 60 * 1000);
        String refreshToken = jwtUtils.generateToken(request.getUsername(), 7L * 24 * 60 * 60 * 1000);

        ResponseCookie response = ResponseCookie.from("refreshToken", refreshToken)
                                                .path("/")
                                                .httpOnly(true)
                                                .maxAge(Duration.ofDays(7))
                                                .sameSite("Strict")
                                                .secure(false)
                                                .build();
    

        return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, response.toString())
                            .body(new TokenResponse(accessToken));
    
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request){
        Cookie[] cookies =  request.getCookies();

        if (cookies == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String refrshtoken = Arrays.stream(cookies)
            .filter(e -> "refreshToken".equals(e.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);
        
        if (refrshtoken == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(!jwtUtils.isTokenExpired(refrshtoken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }   

        String username = jwtUtils.extractUserName(refrshtoken);

        String newAccessToken = jwtUtils.generateToken(username, 15 * 60 * 1000);

        return ResponseEntity.ok().body(Map.of("accessToken", newAccessToken));
    }

}
