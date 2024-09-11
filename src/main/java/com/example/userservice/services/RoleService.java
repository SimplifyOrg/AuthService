package com.example.userservice.services;

import com.example.userservice.DTOs.AddPermissionsToRoleDTO;
import com.example.userservice.DTOs.CreateRoleRequestDTO;
import com.example.userservice.DTOs.RoleDTO;
import com.example.userservice.models.Permission;
import com.example.userservice.models.Role;
import com.example.userservice.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("RoleService")
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    public RoleDTO createRole(CreateRoleRequestDTO createRoleRequestDTO) {
        Role role = new Role();
        role.setName(createRoleRequestDTO.getName());

        Role savedRole = roleRepository.save(role);
        return RoleDTO.from(savedRole);
    }

    public Role addPermissionsToRole(AddPermissionsToRoleDTO addPermissionsToRoleDTO){
        Optional<Role> role = roleRepository.findById(addPermissionsToRoleDTO.getRoleId());
        if(role.isPresent()){
            List<Long> permissionIds = addPermissionsToRoleDTO.getPermissionIds();
            List<Permission> permissions = role.get().getPermissions();
            for (Long permissionId: permissionIds
                 ) {
                Optional<Permission> permission = permissionService.getPermissionById(permissionId);

                // Add permission to role permission list
                // if permission is valid
                if(permission.isPresent()){
                    List<Role> roles = permission.get().getRoles();
                    roles.add(role.get());
                    permission.get().setRoles(roles);
                    permissions.add(permission.get());
                }
            }
            role.get().setPermissions(permissions);
            return roleRepository.save(role.get());
        }
        return null;
    }
}
