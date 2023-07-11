package com.internship.blog.security;

import com.internship.blog.entities.User;
import com.internship.blog.exceptions.ResourceNotFoundException;
import com.internship.blog.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // loading user from database by username
        User user =this.userRepo.findByEmail(username)
                .orElseThrow(()-> new ResourceNotFoundException("User","Email",username));

        return user;
    }
}
