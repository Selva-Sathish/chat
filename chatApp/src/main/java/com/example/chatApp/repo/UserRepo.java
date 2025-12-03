package com.example.chatApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chatApp.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
