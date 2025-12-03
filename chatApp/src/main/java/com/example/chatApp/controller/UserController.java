package com.example.chatApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatApp.models.User;
import com.example.chatApp.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("")
    public List<User> getMethodName() {
        List<User> users = userService.getAllUsers();
        System.out.println(users);
        return users;
    }
    
}
