package com.jwtsecurity.dto;

import com.jwtsecurity.util.Authority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class UserCreationDTO {

    private String message;
    private String id;
    private String userName;
    private String emailId;
    private Authority authority;

    public UserCreationDTO(String message, String id, String userName, String emailId, Authority authority) {
        this.message = message;
        this.id = id;
        this.userName = userName;
        this.emailId = emailId;
        this.authority = authority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserCreationDTO(String id, String userName, String emailId, Authority authority, String message) {
        this.id = id;
        this.userName = userName;
        this.emailId = emailId;
        this.authority = authority;
        this.message = message;
    }

}