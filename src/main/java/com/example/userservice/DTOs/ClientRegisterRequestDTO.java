package com.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;


@Setter
@Getter
public class ClientRegisterRequestDTO {
    private String clientId;
    private String clientSecret;
    private String redirectURI;
    private String postLogoutRedirectURI;
    private Duration accessTokenTTL = Duration.ofMinutes(10);
    private Duration refreshTokenTTL = Duration.ofDays(300);
    private boolean reuseRefreshToken = true;
}
