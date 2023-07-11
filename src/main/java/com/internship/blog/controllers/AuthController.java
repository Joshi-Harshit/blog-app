package com.internship.blog.controllers;

import com.internship.blog.payloads.JwtAuthRequest;
import com.internship.blog.payloads.JwtAuthResponse;
import com.internship.blog.payloads.UserDto;
import com.internship.blog.security.CustomUserDetailService;
import com.internship.blog.security.JwtTokenHelper;
import com.internship.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {


    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken (
            @RequestBody JwtAuthRequest request) throws Exception{

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
        this.doAuthenticate(request.getEmail(), request.getPassword());

        String token = this.jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private void doAuthenticate(String username, String password) throws Exception{

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try{
            this.authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password");
        }

    }

    // Service to register new user

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
        UserDto registeredUser = this.userService.registerNewUser(userDto);
        return new ResponseEntity(registeredUser, HttpStatus.CREATED);
    }


}
