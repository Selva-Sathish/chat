package com.example.chatApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.chatApp.models.User;
import com.example.chatApp.repo.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    public User getByUserName(String name){
        User user = userRepo.findByUserName(name);
        if(user == null){
            throw new UsernameNotFoundException("User Not Found");
        }
        return user;
    }

    public void createUser(User user) {
        if (user != null)
            userRepo.save(user);
    }
    
}
