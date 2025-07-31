package com.example.userservice.controller;

import com.example.userservice.DTOs.PasswordResetRequestDTO;
import com.example.userservice.models.PasswordResetToken;
import com.example.userservice.models.User;
import com.example.userservice.repository.PasswordResetTokenRepository;
import com.example.userservice.services.EmailService;
import com.example.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("")
public class LoginController {

    @Value("${auth.endpoint}")
    private String authEndpoint;

    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public LoginController(UserService userService, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
        this.userService = userService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public String loginPage() {
        System.out.println("Rendering login page");
        return "login"; // returns login.html from /templates
    }

    @PostMapping("/request/password/reset")
    public String requestReset(@RequestBody PasswordResetRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.getUserByEmail(requestDTO.getUser_name());
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("resetError", "No user found with this email.");
            return "redirect:/login";
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
            redirectAttributes.addFlashAttribute("resetError", "Something went wrong. Please try again.");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("resetSuccess", "Password reset link sent successfully!");
        return "redirect:/login";
    }
}
