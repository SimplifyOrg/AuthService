package com.example.userservice.controller;

import com.example.userservice.DTOs.CreateRoleRequestDTO;
import com.example.userservice.DTOs.RoleDTO;
import com.example.userservice.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
