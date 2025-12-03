package com.example.chatApp.service;

import java.util.List;

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
        User user = userRepo.findByUsername(name);
        if(user == null){
            throw new UsernameNotFoundException("User Not Found");
        }
        return user;
    }

    public List<User> getAllUsers(){
        List<User> allUser = userRepo.findAll();
        return allUser;
    }
    
    public void createUser(User user) {
        if (user != null)
            userRepo.save(user);
    }
    
}
