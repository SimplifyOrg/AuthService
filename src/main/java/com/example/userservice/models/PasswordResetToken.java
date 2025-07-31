package com.example.userservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;


@Entity
@Setter
@Getter
public class PasswordResetToken extends BaseModel{

    private String token;

    @Column(nullable = false)
    private String email;

    private ZonedDateTime expiryDate;

    private boolean used;
}
