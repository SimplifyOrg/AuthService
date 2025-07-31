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

    @Value("${auth.endpoint}")
    private String authEndpoint;

    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public UserController(UserService userService, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
        this.userService = userService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
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

    @PostMapping("/request/password/reset")
    public ResponseEntity<String> requestReset(@RequestBody PasswordResetRequestDTO requestDTO) {
        Optional<User> userOpt = userService.getUserByEmail(requestDTO.getUser_name());
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok("If your email exists in our system, you'll receive a reset link shortly.");
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(requestDTO.getUser_name());
        resetToken.setExpiryDate(ZonedDateTime.now().plusMinutes(15));
        resetToken.setUsed(false);
        passwordResetTokenRepository.save(resetToken);

        // Send email with the new password
        String resetLink = "<p>Open the link in browser to reset your password:<p> " + authEndpoint + "/reset/password/" + token;

        try {
            Context context = new Context();
            context.setVariable("resetLink", authEndpoint + "/reset/password/" + token);
            emailService.sendHtmlEmail(userOpt.get().getEmail(), "Password reset", resetLink, "password-reset", context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("If your email exists in our system, you'll receive a reset link shortly.");
    }
}
