package com.example.effective_mobile.controller;

import com.example.effective_mobile.model.User;
import com.example.effective_mobile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class UserController
{
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody User user)
    {
        userService.createUser(user);
    }
}
