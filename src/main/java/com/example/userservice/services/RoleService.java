package com.example.userservice.services;

import com.example.userservice.DTOs.CreateRoleRequestDTO;
import com.example.userservice.DTOs.RoleDTO;
import com.example.userservice.models.Role;
import com.example.userservice.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service("RoleService")
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleDTO createRole(CreateRoleRequestDTO createRoleRequestDTO) {
        Role role = new Role();
        role.setName(createRoleRequestDTO.getName());

        Role savedRole = roleRepository.save(role);
        return RoleDTO.from(savedRole);
    }
}
