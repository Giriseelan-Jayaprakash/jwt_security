package com.jwtsecurity.service;

import com.jwtsecurity.model.Users;
import com.jwtsecurity.util.UserValidation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//import  com.jwtsecurity.util.UserValidation.isValidEmail;

@Service
public class JWTService {
    // to hardcore token
//    public String generateToken(){
//        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkdpcmlzZWVsYW4gSmF5YXByYWthc2giLCJpYXQiOjE1MTYyMzkwMjIsImV4cCI6MTUxNjI1OTAyMn0.xWJj1HUGY6tO9YtUF7jjmY7XiiIijwL2X09RwkObnNo";
//    }

    // to generate token using jwts
    @Autowired
    private UserValidation userValidation;

    public String generateToken(Users user) {
//        System.err.println(userValidation.isValidEmail(user.getEmailId()) + "  " + userValidation.isValidPassword(user.getPassword()));
//        userValidation.areCredentialsValid(user.getEmailId(),user.getPassword());
//            throw new IllegalArgumentException("Invalid EmailId or Password");
//        }
        if (user == null && user.getId() == null) {
            throw new IllegalArgumentException("User or User ID is null. Cannot generate token.");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("emailId", user.getEmailId());
        claims.put("name", user.getUserName());
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 600 * 60 * 30))
                .and()
                .signWith(getKey())
                .compact();
    }

    private String secretkey = "";

    public JWTService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey skey = keyGenerator.generateKey();
            secretkey = Base64.getEncoder().encodeToString(skey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
//        System.err.println(" test .."+extractClaim(token, Claims::getSubject));
        return extractClaim(token, claims -> claims.get("emailId", String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
//        System.err.println("te  //"+userName+"   "+userDetails.ge);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
