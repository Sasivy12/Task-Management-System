package com.example.effective_mobile.controller;

import com.example.effective_mobile.model.User;
import com.example.effective_mobile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController
{
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody User user)
    {
        userService.register(user);
    }

    @GetMapping()
    public List<User> returnAllUsers()
    {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public String login(@RequestBody User user)
    {
        return userService.verify(user);
    }
}
