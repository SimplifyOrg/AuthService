package com.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordUpdateRequestDTO {
    private String oldPassword;
    private String newPassword;
}
