package com.jwtsecurity.service;

import com.jwtsecurity.model.Users;
import com.jwtsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public String createUser(final Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        System.err.println(userRepository.existsByEmailId(user.getEmailId()));
        if (userRepository.existsByEmailId(user.getEmailId())) throw new IllegalArgumentException("User Already exits");
        userRepository.save(user);
        return "Created Successfully";
    }

    public String verify(final Users user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmailId(), user.getPassword()));
        if (authentication.isAuthenticated())
//            return "Login Successfull";
//            return jwtService.generateToken(); // for hardcoding token
            return jwtService.generateToken(user);
        return "Invalid Login";
    }
}
