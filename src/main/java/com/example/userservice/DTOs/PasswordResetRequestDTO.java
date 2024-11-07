package com.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordResetRequestDTO {
    private String user_name;
}
