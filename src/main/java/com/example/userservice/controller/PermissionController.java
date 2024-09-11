package com.example.userservice.controller;

import com.example.userservice.DTOs.CreatePermissionRequestDTO;
import com.example.userservice.DTOs.PermissionResponseDTO;
import com.example.userservice.models.Permission;
import com.example.userservice.services.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/create")
    public ResponseEntity<PermissionResponseDTO> createPermission(@RequestBody CreatePermissionRequestDTO createPermissionRequestDTO){
        Permission permission = permissionService.createPermission(createPermissionRequestDTO);
        return new ResponseEntity<>(PermissionResponseDTO.from(permission), HttpStatus.OK);
    }
}
