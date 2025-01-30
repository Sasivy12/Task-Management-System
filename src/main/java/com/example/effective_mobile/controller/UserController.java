package com.example.effective_mobile.controller;

import com.example.effective_mobile.model.User;
import com.example.effective_mobile.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Tag(name = "User Management", description = "API for managing user registration and login")
public class UserController
{
    @Autowired
    private UserService userService;

    @Operation(summary = "Register user", description = "Registers a new user")
    @PostMapping("/register")
    public void registerUser(@RequestBody User user)
    {
        userService.register(user);
    }

    @Operation(summary = "Logs in a user", description = "Logs in an existing user")
    @PostMapping("/login")
    public String login(@RequestBody User user)
    {
        return userService.verify(user);
    }
}
