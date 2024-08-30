ALTER TABLE authorization
    MODIFY access_token_metadata LONGTEXT;

ALTER TABLE authorization
    MODIFY access_token_scopes LONGTEXT;

ALTER TABLE authorization
    MODIFY access_token_value LONGTEXT;

ALTER TABLE authorization
    MODIFY attributes LONGTEXT;

ALTER TABLE authorization_consent
    MODIFY authorities LONGTEXT;

ALTER TABLE authorization
    MODIFY authorization_code_value LONGTEXT;

ALTER TABLE client
    MODIFY authorization_grant_types LONGTEXT;

ALTER TABLE authorization
    MODIFY authorized_scopes LONGTEXT;

ALTER TABLE client
    MODIFY client_authentication_methods LONGTEXT;

ALTER TABLE client
    MODIFY client_settings LONGTEXT;

ALTER TABLE authorization
    MODIFY device_code_metadata LONGTEXT;

ALTER TABLE authorization
    MODIFY device_code_value LONGTEXT;

ALTER TABLE authorization
    MODIFY oidc_id_token_claims LONGTEXT;

ALTER TABLE authorization
    MODIFY oidc_id_token_metadata LONGTEXT;

ALTER TABLE authorization
    MODIFY oidc_id_token_value LONGTEXT;

ALTER TABLE client
    MODIFY post_logout_redirect_uris LONGTEXT;

ALTER TABLE client
    MODIFY redirect_uris LONGTEXT;

ALTER TABLE authorization
    MODIFY refresh_token_metadata LONGTEXT;

ALTER TABLE authorization
    MODIFY refresh_token_value LONGTEXT;

ALTER TABLE client
    MODIFY scopes LONGTEXT;

ALTER TABLE session
    MODIFY token LONGTEXT;

ALTER TABLE client
    MODIFY token_settings LONGTEXT;

ALTER TABLE authorization
    MODIFY user_code_metadata LONGTEXT;

ALTER TABLE authorization
    MODIFY user_code_value LONGTEXT;