package com.example.userservice.security;

import com.example.userservice.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Setter
@Getter
@JsonDeserialize(as = CustomGrantedAuthority.class)
public class CustomGrantedAuthority implements GrantedAuthority {

    private Role role;
    public CustomGrantedAuthority(){}
    public CustomGrantedAuthority(Role role){
        this.role = role;
    }
    @Override
    @JsonIgnore
    public String getAuthority() {
        return role.getName();
    }
}
