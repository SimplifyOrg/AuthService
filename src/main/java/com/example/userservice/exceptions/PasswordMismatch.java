package com.example.userservice.exceptions;

public class PasswordMismatch extends Exception{
    public PasswordMismatch() {
        super("Wrong password");
    }
}
