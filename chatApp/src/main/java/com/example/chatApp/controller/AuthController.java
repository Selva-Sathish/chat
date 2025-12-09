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
import org.springframework.security.core.userdetails.UserDetails;

import com.example.chatApp.dto.LoginRequest;
import com.example.chatApp.dto.UserReponse;
import com.example.chatApp.mapper.UserMapper;
import com.example.chatApp.models.User;
import com.example.chatApp.repo.UserRepo;
import com.example.chatApp.service.UserService;
import com.example.chatApp.security.CustomUserDetails;
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
    private final UserService userService;

    public AuthController(UserDetailService userDetailService,
             AuthenticationManager authenticationManager,
             JwtUtils jwtUtils,
             UserService userService
        ){
        this.userDetailService = userDetailService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authencation(@RequestBody LoginRequest request){
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        String accessToken = jwtUtils.generateToken(request.getUsername(), "at", 15 * 60 * 1000);
        String refreshToken = jwtUtils.generateToken(request.getUsername(), "rt", 7L * 24 * 60 * 60 * 1000);
        
        ResponseCookie responseAt = ResponseCookie.from("at", accessToken)
                                                .path("/")
                                                .httpOnly(true)
                                                .maxAge(15 * 60)
                                                .sameSite("Strict")
                                                .secure(false)
                                                .build();

        ResponseCookie responseRt = ResponseCookie.from("rt", refreshToken)
                                                .path("/")
                                                .httpOnly(true)
                                                .maxAge(7 * 24 * 60 * 60)
                                                .sameSite("Strict")
                                                .secure(false)
                                                .build();
        
        User userDate = userService.getByUserName(request.getUsername());
        UserReponse reponse = UserMapper.toResReponse(userDate);

        return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, responseAt.toString())
                    .header(HttpHeaders.SET_COOKIE, responseRt.toString())
                    .body(reponse);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request){
        
        Cookie[] cookies =  request.getCookies();   

        if (cookies == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        

        String refrshtoken = Arrays.stream(cookies)
            .filter(e -> "rt".equals(e.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);
        
        System.out.println(refrshtoken);

        if (refrshtoken == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        if(jwtUtils.isTokenExpired(refrshtoken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }   

        System.out.println("after checking the token expiration ");
        String username = jwtUtils.extractUserName(refrshtoken);
        
        User currUser = userService.getByUserName(username);
        
        String newAccessToken = jwtUtils.generateToken(username, "at", 10 * 60 * 1000);
        
        ResponseCookie responseCookie = ResponseCookie.from("at", newAccessToken)
                                            .maxAge(10*60)
                                            .secure(false)
                                            .httpOnly(true)
                                            .sameSite("Strict")
                                            .path("/")
                                            .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(UserMapper.toResReponse(currUser));
    }

    @PostMapping("me")
    public ResponseEntity<?> me(Authentication auth) {
        // System.out.println((UserDetails)auth.getDetails() + "userdetails");
        
        if (auth == null) {
            return ResponseEntity.status(401).build();
        }

        String username = auth.getName();
        User user = userService.getByUserName(username);
        System.out.println(username + "authme  ------>");        
        return ResponseEntity.ok().body(UserMapper.toResReponse(user));
    }
    
}
