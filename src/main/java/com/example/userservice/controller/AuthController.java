package com.example.userservice.controller;

import com.example.userservice.DTOs.*;
import com.example.userservice.annotations.UserEmail;
import com.example.userservice.exceptions.ExistingUser;
import com.example.userservice.exceptions.PasswordMismatch;
import com.example.userservice.exceptions.SessionNotFound;
import com.example.userservice.exceptions.UserNotFound;
import com.example.userservice.models.PasswordResetToken;
import com.example.userservice.models.SessionStatus;
import com.example.userservice.models.User;
import com.example.userservice.repository.PasswordResetTokenRepository;
import com.example.userservice.services.AuthService;
import com.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthController(AuthService authService, UserService userService, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.authService = authService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) throws UserNotFound, PasswordMismatch {
        return authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) throws SessionNotFound {
        return authService.logout(logoutRequestDTO.getToken(), logoutRequestDTO.getUserId());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignupRequestDTO signupRequestDTO) throws ExistingUser {
        UserDTO userDTO = authService.signup(signupRequestDTO.getEmail(), signupRequestDTO.getPassword());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenDTO validateTokenDTO) {
        SessionStatus sessionStatus = authService.validate(validateTokenDTO.getToken());
        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }

    @PutMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PasswordUpdateRequestDTO passwordUpdateDTO) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            if(!authService.updatePassword(token, passwordUpdateDTO.getUser_name(), passwordUpdateDTO.getOldPassword(), passwordUpdateDTO.getNewPassword())){
                throw new UserNotFound(passwordUpdateDTO.getUser_name());
            }
            return ResponseEntity.ok("Password updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is not registered");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating password");
        }
    }

//    @PostMapping("/reset/password")
//    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO) {
//        try {
//            authService.resetPassword(passwordResetRequestDTO.getUser_name());
//            return ResponseEntity.ok("A new password has been sent to your email address.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
//        }
//    }

    @GetMapping("/reset/password/{token}")
    public ResponseEntity<String> handleResetLink(@PathVariable String token) throws UserNotFound {
        Optional<PasswordResetToken> resetOpt = passwordResetTokenRepository.findByTokenAndUsedFalse(token);

        if (resetOpt.isEmpty() || resetOpt.get().getExpiryDate().isBefore(ZonedDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset link.");
        }

        authService.resetPassword(resetOpt.get().getEmail());
        resetOpt.get().setUsed(true);
        passwordResetTokenRepository.save(resetOpt.get());
        return ResponseEntity.ok("A new password has been sent to your email.");
    }
}
