package com.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ValidateTokenDTO {
    private String token;
    private Long userId;
}
