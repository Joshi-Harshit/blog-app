package com.internship.blog.services.impl;

import com.internship.blog.config.AppConstants;
import com.internship.blog.entities.Role;
import com.internship.blog.entities.User;
import com.internship.blog.exceptions.ResourceNotFoundException;
import com.internship.blog.payloads.UserDto;
import com.internship.blog.repositories.RoleRepo;
import com.internship.blog.repositories.UserRepo;
import com.internship.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepo roleRepo;


    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        // encode the password
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        //roles
        Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
        user.getRoles().add(role);

        User savedUser = this.userRepo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepo.save(user);

        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user = this.userRepo.findById(userId).
                orElseThrow(()-> new ResourceNotFoundException("User","Id",userId.toString()));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepo.save(user);

        return this.userToDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {

        User user = this.userRepo.findById(userId).
                orElseThrow(()-> new ResourceNotFoundException("User","Id",userId.toString()));

        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();

        List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {

        User user = this.userRepo.findById(userId).
                orElseThrow(()-> new ResourceNotFoundException("User","Id",userId.toString()));

        this.userRepo.delete(user);

    }

    private User dtoToUser(UserDto userDto){

        User user = this.modelMapper.map(userDto, User.class);
        return user;

//        User user = new User();
//        user.setId(userDto.getId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setAbout(userDto.getAbout());
//        user.setPassword(userDto.getPassword());
//        return user;
    }

    private UserDto userToDto(User user){

        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        return userDto;

//        UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        userDto.setAbout(user.getAbout());
//        userDto.setName(user.getName());
//        userDto.setEmail(user.getEmail());
//        userDto.setPassword(user.getPassword());
//        return userDto;
    }


}
