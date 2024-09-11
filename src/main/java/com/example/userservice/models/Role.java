package com.example.userservice.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@JsonDeserialize(as = Role.class)
public class Role extends BaseModel{
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<Permission> permissions;
}
