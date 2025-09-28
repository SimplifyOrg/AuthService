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
    private boolean requireProofKey;
    private Duration accessTokenTTL = Duration.ofMinutes(10);
    private Duration refreshTokenTTL = Duration.ofDays(3000);
    private boolean reuseRefreshToken = true;
}
