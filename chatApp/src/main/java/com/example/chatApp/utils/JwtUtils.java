package com.example.chatApp.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    
    @Value("${jwt.secret}")
    private String key;

    public SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String type, long expiry){
        
        return Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date())
                .claim("type", type)
                .expiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Claims tokenParser(String token){
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractTokenType(String token){
        return tokenParser(token).get("type", String.class);
    }

    public String extractUserName(String token) {
        String username = tokenParser(token).getSubject();
        return username;
    }

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUserName(token);
        return (userDetails.getUsername().equals(username) && isTokenExpired(token));
	} 
    
    public boolean isTokenExpired(String token){
        return tokenParser(token).getExpiration().after(new Date());
    }

}
