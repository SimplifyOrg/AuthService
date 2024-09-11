package com.example.userservice.services;

import com.example.userservice.DTOs.CreatePermissionRequestDTO;
import com.example.userservice.models.Permission;
import com.example.userservice.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("PermissionService")
public class PermissionService {

   private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(CreatePermissionRequestDTO permissionRequestDTO){
        Permission permission = new Permission();
        permission.setName(permissionRequestDTO.getName());

        return permissionRepository.save(permission);
    }

    public Optional<Permission> getPermissionById(Long permissionId) {
        return permissionRepository.findById(permissionId);
    }
}
