package com.example.userservice.security.repository;

import java.util.Optional;


import com.example.userservice.security.models.Authorization;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, String> {
    Optional<Authorization> findByState(String state);
    Optional<Authorization> findByAuthorizationCodeValue(String authorizationCode);
    Optional<Authorization> findByAccessTokenValue(String accessToken);
    Optional<Authorization> findByRefreshTokenValue(String refreshToken);
    Optional<Authorization> findByOidcIdTokenValue(String idToken);
    Optional<Authorization> findByUserCodeValue(String userCode);
    Optional<Authorization> findByDeviceCodeValue(String deviceCode);
    @Query("select a from Authorization a where a.state = :token" +
            " or a.authorizationCodeValue = :token" +
            " or a.accessTokenValue = :token" +
            " or a.refreshTokenValue = :token" +
            " or a.oidcIdTokenValue = :token" +
            " or a.userCodeValue = :token" +
            " or a.deviceCodeValue = :token"
    )
    Optional<Authorization> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(@Param("token") String token);

    // 1. Delete all tokens for a specific principal
    @Modifying
    @Transactional
    void deleteByPrincipalName(String principalName);

    // 2. Delete specific token for a principal by access token value
    @Modifying
    @Transactional
    @Query("DELETE FROM Authorization a WHERE a.principalName = :principalName AND a.refreshTokenValue = :refreshToken")
    void deleteByPrincipalNameAndRefreshTokenValue(@Param("principalName") String principalName,
                                                  @Param("refreshToken") String refreshToken);
}
