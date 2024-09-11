package com.example.userservice.controller;

import com.example.userservice.DTOs.AddPermissionsToRoleDTO;
import com.example.userservice.DTOs.CreateRoleRequestDTO;
import com.example.userservice.DTOs.RoleDTO;
import com.example.userservice.exceptions.RoleNotFound;
import com.example.userservice.models.Role;
import com.example.userservice.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    public ResponseEntity<RoleDTO> createRole(@RequestBody CreateRoleRequestDTO createRoleRequestDTO) {
        RoleDTO roleDTO = roleService.createRole(createRoleRequestDTO);
        return new ResponseEntity<>(roleDTO, HttpStatus.OK);
    }

    @PutMapping("/add/permissions")
    public ResponseEntity<RoleDTO> addPermissionsToRole(@RequestBody AddPermissionsToRoleDTO addPermissionsToRoleDTO) throws RoleNotFound {
        Role role = roleService.addPermissionsToRole(addPermissionsToRoleDTO);
        if(role == null){
            throw new RoleNotFound();
        }
        return new ResponseEntity<>(RoleDTO.from(role), HttpStatus.OK);
    }
}
