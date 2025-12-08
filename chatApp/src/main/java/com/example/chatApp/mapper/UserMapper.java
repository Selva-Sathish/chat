package com.example.chatApp.mapper;

import com.example.chatApp.dto.UserReponse;
import com.example.chatApp.models.User;

public class UserMapper {
    
    public static UserReponse toResReponse(User user){
        UserReponse reponse = new UserReponse();
        reponse.setId(user.getId());
        reponse.setUsername(user.getUsername());
        reponse.setNumber(user.getNumber());
        reponse.setRole(user.getRole().name());
        return reponse;
    }
}
