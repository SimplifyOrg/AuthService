package com.example.userservice.DTOs;

import com.example.userservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleDTO {
    private Long roleId;
    private String name;

    public static RoleDTO from(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(role.getId());
        roleDTO.setName(role.getName());

        return roleDTO;
    }
}
