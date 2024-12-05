package com.example.effective_mobile.service;

import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        User user = userRepository.findByEmail(email);

        if(user == null)
        {
            throw new UsernameNotFoundException("User not found");
        }
        else
        {
            return new com.example.effective_mobile.model.UserDetails(user);
        }
    }
}
