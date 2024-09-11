package com.example.userservice.DTOs;

import com.example.userservice.models.Permission;
import com.example.userservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class RoleDTO {
    private Long roleId;
    private String name;
    private List<Long> permissions = new ArrayList<>();

    public static RoleDTO from(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(role.getId());
        roleDTO.setName(role.getName());
        List<Long> permissionList = new ArrayList<>();
        for (Permission permission : role.getPermissions()
             ) {
            permissionList.add(permission.getId());
        }
        roleDTO.setPermissions(permissionList);

        return roleDTO;
    }
}
