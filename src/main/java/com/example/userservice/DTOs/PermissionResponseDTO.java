package com.example.userservice.DTOs;

import com.example.userservice.models.Permission;
import com.example.userservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PermissionResponseDTO {
    private String name;
    private List<Long> roles;

    public static PermissionResponseDTO from(Permission permission){
        PermissionResponseDTO permissionResponseDTO = new PermissionResponseDTO();
        permissionResponseDTO.setName(permission.getName());
        List<Long> roleIds = new ArrayList<>();
        for (Role role: permission.getRoles()
             ) {
            roleIds.add(role.getId());
        }
        permissionResponseDTO.setRoles(roleIds);
        return permissionResponseDTO;
    }
}
