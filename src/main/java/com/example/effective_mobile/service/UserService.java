package com.example.effective_mobile.service;

import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserRepository repository;

    public void createUser(User user)
    {
        if(repository.existsByEmail(user.getEmail()))
        {
            System.out.println("User already exists");
        }
        else
        {
            repository.save(user);
        }
    }

}
