package com.example.userservice.security;

import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@JsonDeserialize(as = CustomUserDetails.class)
public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails() {}
    public CustomUserDetails(User user){
        this.user = user;
    }
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();

        Collection<CustomGrantedAuthority> customGrantedAuthorities = new ArrayList<>();
        for(Role role : roles){
            customGrantedAuthorities.add(new CustomGrantedAuthority(role));
        }
        return customGrantedAuthorities;
    }

    @JsonIgnore
    public Long getId(){
        return user.getId();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
