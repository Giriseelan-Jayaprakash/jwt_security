package com.jwtsecurity.service;

import com.jwtsecurity.dto.ResponseDTO;
import com.jwtsecurity.dto.UserCreationDTO;
import com.jwtsecurity.exception.ExceptionHandling;
import com.jwtsecurity.model.UserPrincipal;
import com.jwtsecurity.model.Users;
import com.jwtsecurity.repository.UserRepository;
import com.jwtsecurity.util.UserValidation;
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
    @Autowired
    private UserValidation userValidation;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public UserCreationDTO createUser(final Users user)  {
        if (!userValidation.isValidEmail(user.getEmailId())) {
        throw new ExceptionHandling("Invalid Email format");
    }
        if (!userValidation.isValidPassword(user.getPassword())) {
            throw new ExceptionHandling("Invalid Password format");
        }
        if (userRepository.existsByEmailId(user.getEmailId())) {
            throw new ExceptionHandling("User Already exists");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new UserCreationDTO("Created Successfully",user.getId(),user.getUserName(),user.getEmailId(),user.getAuthority());
    }

    public ResponseDTO verify(final Users user) {
        if (!userValidation.isValidEmail(user.getEmailId()) || !userValidation.isValidPassword(user.getPassword())) {
            throw new ExceptionHandling("Invalid Email or Password format");
        }
        Users storedUser = userRepository.findByEmailId(user.getEmailId());

        if (storedUser == null) {
            return new ResponseDTO("User not found", null);
        }
        if (bCryptPasswordEncoder.matches(user.getPassword(), storedUser.getPassword())) {
            String token = jwtService.generateToken(storedUser);  // Assume this method generates the token
            return new ResponseDTO("Login Successful",token);
        } else {
            return new ResponseDTO("Invalid login credentials",null);
        }

    }

}
