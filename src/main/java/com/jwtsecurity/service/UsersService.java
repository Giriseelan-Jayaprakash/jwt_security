package com.jwtsecurity.service;

import com.jwtsecurity.dto.ResponseDTO;
import com.jwtsecurity.exception.ExceptionHandling;
import com.jwtsecurity.model.Users;
import com.jwtsecurity.repository.UserRepository;
import com.jwtsecurity.util.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    public ResponseDTO createUser(final Users user) {
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
//        Users users1 = new Users(users1.setId(user.getId()),users1.setUserName(user.getUserName()),users1.setEmailId(user.getEmailId()),users1.setAuthority(user.getAuthority()));
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", user.getId());
        responseData.put("userName", user.getUserName());
        responseData.put("emailId", user.getEmailId());
        responseData.put("authority", user.getAuthority());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatusCode(HttpStatus.CREATED.value());
        responseDTO.setMessage("Created Successfully");
        responseDTO.setData(responseData);
        return responseDTO;
    }

    public ResponseDTO verify(final Users user) {
        if (!userValidation.isValidEmail(user.getEmailId()) || !userValidation.isValidPassword(user.getPassword())) {
            throw new ExceptionHandling("Invalid Email or Password format");
        }
        Users storedUser = userRepository.findByEmailId(user.getEmailId());
        ResponseDTO responseDTO = new ResponseDTO();
        if (storedUser == null) {
            responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
            responseDTO.setMessage("User not found");
            responseDTO.setData(null);
            return responseDTO;
        }
        if (bCryptPasswordEncoder.matches(user.getPassword(), storedUser.getPassword())) {
//            String token = jwtService.generateToken(storedUser);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", storedUser.getId());
            responseData.put("userName", storedUser.getUserName());
            responseData.put("emailId", storedUser.getEmailId());
            responseData.put("authority", storedUser.getAuthority());
            responseData.put("token", jwtService.generateToken(storedUser));
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setMessage("Login Successful");
            responseDTO.setData(responseData);
            return responseDTO;
        } else {
            responseDTO.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            responseDTO.setMessage("Invalid login credentials");
            responseDTO.setData(null);
            return responseDTO;
        }
    }
}
