package com.example.chatApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.chatApp.models.User;
import com.example.chatApp.service.UserService;


@SpringBootApplication
@EnableCaching
public class ChatAppApplication implements CommandLineRunner{
	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(ChatAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		User user = new User();
		user.setNumber("9789432788");
		user.setUserName("sathish");
		user.setPassword(passwordEncoder.encode("sathihs@123"));
		
		userService.createUser(user);
	}

}
