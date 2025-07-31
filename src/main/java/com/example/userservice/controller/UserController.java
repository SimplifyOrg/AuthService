package com.example.userservice.controller;

import com.example.userservice.DTOs.PasswordResetRequestDTO;
import com.example.userservice.DTOs.SetRolesRequestDTO;
import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.models.PasswordResetToken;
import com.example.userservice.models.User;
import com.example.userservice.repository.PasswordResetTokenRepository;
import com.example.userservice.services.EmailService;
import com.example.userservice.services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable("id")Long userId) {
        UserDTO userDTO = userService.getUser(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDTO> setUserRoles(@PathVariable("id") Long userId, @RequestBody SetRolesRequestDTO request) {
        List<Long> roles = request.getRoleIds();
        UserDTO userDTO = userService.setUserRoles(userId, roles);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
