package com.example.userservice.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@JsonDeserialize(as = Role.class)
public class Role extends BaseModel{
    private String name;
}
