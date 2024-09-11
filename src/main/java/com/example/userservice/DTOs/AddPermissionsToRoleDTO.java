package com.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AddPermissionsToRoleDTO {
    private Long roleId;
    private List<Long> permissionIds;
}
