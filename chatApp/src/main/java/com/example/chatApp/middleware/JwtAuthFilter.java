package com.example.chatApp.middleware;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.chatApp.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService){
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService; 
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
                String token = extractAccessTokenFromCookies(request, "at");
                String rtToken = extractAccessTokenFromCookies(request, "rt");
                
                if(token == null){
                    filterChain.doFilter(request, response);
                    return;
                }

                
                // if(jwtUtils.isTokenExpired(token)){
                //     System.out.println("token expired in the middleware");
                //     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                //     response.setContentType("application/json");
                //     response.getWriter().write("{\"error\": \"Token missing\"}");
                //     return;
                // }
                
                String username = jwtUtils.extractUserName(token);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(username != null && authentication == null){
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if(jwtUtils.isTokenValid(token, userDetails)){
                        UsernamePasswordAuthenticationToken authenticationToken = 
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }

                filterChain.doFilter(request, response);
            }

    private String extractAccessTokenFromCookies(HttpServletRequest request, String type) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;
        return Arrays.stream(cookies).filter(c -> c.getName().equals(type)).map(Cookie::getValue).findFirst().orElse(null);
    }
    
}
