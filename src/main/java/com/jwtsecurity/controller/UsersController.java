package com.jwtsecurity.controller;

import com.jwtsecurity.dto.ResponseDTO;
import com.jwtsecurity.dto.UserCreationDTO;
import com.jwtsecurity.model.Users;
import com.jwtsecurity.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create-user")
    public UserCreationDTO createUser(@RequestBody final Users user) {
        return usersService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody final Users user) {
//        System.out.println(user);
//        return "Login Successfull";
        return usersService.verify(user);

    }
}
