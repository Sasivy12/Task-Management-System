package com.example.effective_mobile.service;

import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void register(User user)
    {
        if(repository.existsByEmail(user.getEmail()))
        {
            System.out.println("User already exists");
        }
        else
        {
            user.setPassword(encoder.encode(user.getPassword()));
            repository.save(user);
        }
    }

    public List<User> getAllUsers()
    {
        return repository.findAll();
    }

    public String verify(User user)
    {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if(authentication.isAuthenticated())
        {
            String token = jwtService.generateToken(user.getEmail());
            return token;
        }
        return "FAILED";
    }

}
