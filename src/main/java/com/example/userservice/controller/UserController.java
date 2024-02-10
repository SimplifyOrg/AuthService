package com.example.userservice.controller;

import com.example.userservice.DTOs.SetRolesRequestDTO;
import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable("id")UUID userId) {
        UserDTO userDTO = userService.getUser(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDTO> setUserRoles(@PathVariable("id") UUID userId, @RequestBody SetRolesRequestDTO request) {
        List<UUID> roles = request.getRoleIds();
        UserDTO userDTO = userService.setUserRoles(userId, roles);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
