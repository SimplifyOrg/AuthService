package com.example.userservice.exceptions;

import java.util.UUID;

public class SessionNotFound  extends Exception{
    public SessionNotFound() {
        super("User session is invalid");
    }
}
