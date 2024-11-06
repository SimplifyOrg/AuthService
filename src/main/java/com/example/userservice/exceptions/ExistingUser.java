package com.example.userservice.exceptions;

public class ExistingUser extends Exception{
    public ExistingUser(String email) {
        super("Email " + email + " is already registered.");
    }
}
