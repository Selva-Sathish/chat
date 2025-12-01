package com.example.chatApp.security;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.chatApp.models.User;
import com.example.chatApp.service.UserService;

@Service
public class UserDetailService implements UserDetailsService {
    
    private final UserService userService;

    public UserDetailService(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUserName(username);
        return new CustomUserDetails(user);
        
    }
    
}
