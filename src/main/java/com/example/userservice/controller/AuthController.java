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

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) throws UserNotFound, PasswordMismatch {
        return authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) throws SessionNotFound, UserNotFound {
        return authService.logout(logoutRequestDTO.getToken(), logoutRequestDTO.getEmail());
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
}
