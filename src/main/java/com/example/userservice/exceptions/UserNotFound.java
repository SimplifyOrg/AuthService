package com.example.userservice.exceptions;

public class UserNotFound extends Exception{
    public UserNotFound(String email) {
        super("Login failed for " + email);
    }
}
