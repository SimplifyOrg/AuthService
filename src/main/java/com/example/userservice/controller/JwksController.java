package com.example.userservice.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JwksController {

    private final JWKSource<SecurityContext> jwkSource;

    @Autowired
    public JwksController(JWKSource<SecurityContext> jwkSource) {
        this.jwkSource = jwkSource;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getJwks() {
        try {
            JWKSelector selector = new JWKSelector(new JWKMatcher.Builder().build()); // matches all keys
            List<JWK> jwks = jwkSource.get(selector, null); // now safe to call
            return new JWKSet(jwks).toJSONObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JWKS", e);
        }
    }
}
