package com.jwtsecurity.util;

import com.jwtsecurity.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidation {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        public  boolean isValidEmail(String email) {
            return email != null && Pattern.matches(EMAIL_REGEX, email.trim());
        }

        public  boolean isValidPassword(String password) {
//            System.out.println("Validating Password: " + password);
            return password != null && Pattern.matches(PASSWORD_REGEX, password);
        }
        public void areCredentialsValid(String emailId,String password){
//            System.err.println(isValidPassword(password));
            if(!isValidEmail(emailId)) throw  new IllegalArgumentException("Email is not a valid format");
            if(!isValidPassword(password)) throw  new IllegalArgumentException("Password is not a valid format");

        }
    }
