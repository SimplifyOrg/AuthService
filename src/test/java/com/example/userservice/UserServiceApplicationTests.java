package com.example.userservice;

import com.example.userservice.security.repository.JpaRegisteredClientRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.test.annotation.Commit;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.time.Duration;
import java.util.UUID;

import static java.time.Duration.ofDays;

@SpringBootTest
class UserServiceApplicationTests {

    @Inject
    JpaRegisteredClientRepository registeredClientRepository;
    @Inject
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    @Commit
    void registerApiService() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("api-service")
                .clientSecret(bCryptPasswordEncoder.encode("apiservicetest"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://192.168.1.5:8080")
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .redirectUri("com.example.ui://home")
                .redirectUri("com.example.ui://books")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(10))  // Set access token lifetime
                        .refreshTokenTimeToLive(Duration.ofDays(300)) // Set refresh token lifetime
                        .reuseRefreshTokens(true)  // Allow refresh token reuse
                        .build())
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        registeredClientRepository.save(oidcClient);
    }

    @Test
    @Commit
    void registerFayolClient() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("fayol-service")
                .clientSecret(bCryptPasswordEncoder.encode("fayoltest"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://192.168.1.9:8082")
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .postLogoutRedirectUri("http://127.0.0.1:8082/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        registeredClientRepository.save(oidcClient);
    }

}
