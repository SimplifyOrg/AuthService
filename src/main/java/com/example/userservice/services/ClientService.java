package com.example.userservice.services;

import com.example.userservice.DTOs.ClientRegisterRequestDTO;
import com.example.userservice.security.repository.JpaRegisteredClientRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("ClientService")
public class ClientService {

    private final JpaRegisteredClientRepository registeredClientRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ClientService(JpaRegisteredClientRepository registeredClientRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.registeredClientRepository = registeredClientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void registerClient(ClientRegisterRequestDTO clientRegisterRequestDTO){
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientRegisterRequestDTO.getClientId())
                .clientSecret(bCryptPasswordEncoder.encode(clientRegisterRequestDTO.getClientSecret()))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(clientRegisterRequestDTO.getRedirectURI())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(clientRegisterRequestDTO.getAccessTokenTTL())  // Set access token lifetime
                        .refreshTokenTimeToLive(clientRegisterRequestDTO.getRefreshTokenTTL()) // Set refresh token lifetime
                        .reuseRefreshTokens(clientRegisterRequestDTO.isReuseRefreshToken())  // Allow refresh token reuse
                        .build())
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        registeredClientRepository.save(oidcClient);
    }
}
