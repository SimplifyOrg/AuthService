package com.example.userservice.services;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.exceptions.ExistingUser;
import com.example.userservice.exceptions.PasswordMismatch;
import com.example.userservice.exceptions.SessionNotFound;
import com.example.userservice.exceptions.UserNotFound;
import com.example.userservice.models.Session;
import com.example.userservice.models.SessionStatus;
import com.example.userservice.models.User;
import com.example.userservice.repository.SessionRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.models.Authorization;
import com.example.userservice.security.repository.AuthorizationRepository;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.time.DateUtils;
import java.security.SecureRandom;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import org.springframework.util.MultiValueMapAdapter;
import org.thymeleaf.context.Context;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

@Service("AuthService")
public class AuthService {

    private final SessionRepository sessionRepository;
    private final AuthorizationRepository authorizationRepository;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final EmailService emailService;

    private String generateRandomPassword() {
        // Secure random password generation
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); // Generates a URL-safe base64 string
    }

    public AuthService(SessionRepository sessionRepository, AuthorizationRepository authorizationRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService) {
        this.sessionRepository = sessionRepository;
        this.authorizationRepository = authorizationRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
    }

    public ResponseEntity<UserDTO> login(String email, String password) throws UserNotFound, PasswordMismatch {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotFound(email);
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new PasswordMismatch();
        }

        MacAlgorithm algo = Jwts.SIG.HS256;
        SecretKey key = algo.key().build();

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("email", user.getEmail());
        jsonMap.put("roles", List.of(user.getRoles()));
        jsonMap.put("createdAt", new Date());
        jsonMap.put("expiry", DateUtils.addDays(new Date(), 30));

        String jws = Jwts.builder()
                .claims(jsonMap)
                .signWith(key, algo)
                .compact();

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(jws);
        session.setUser(user);
        sessionRepository.save(session);

        UserDTO userDTO = UserDTO.from(user);

        MultiValueMapAdapter<String, String> header = new MultiValueMapAdapter<>(new HashMap<>());
        header.add(HttpHeaders.SET_COOKIE, "auth-token:" + jws);

        return new ResponseEntity<>(userDTO, header, HttpStatus.OK);
    }

    public ResponseEntity<Void> logout(String token, Long userId) throws SessionNotFound {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty()) {
            throw new SessionNotFound();
        }

        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();
    }

    public UserDTO signup(String email, String password) throws ExistingUser {

        Optional<User> oldUser = userRepository.findByEmail(email);
        if(oldUser.isPresent()){
            throw new ExistingUser(email);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        return UserDTO.from(savedUser);
    }

    public SessionStatus validate(String token) {
//        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        Optional<Authorization> authorization = authorizationRepository.findByAccessTokenValue(token);
        if(authorization.isEmpty()){
            return SessionStatus.ENDED;
        }

        Instant now = Instant.now();
        if(authorization.get().getAccessTokenExpiresAt().isBefore(now)){
            return SessionStatus.ENDED;
        }

        //JWT Decoding.
        //Jws<Claims> jwsClaims = Jwts.parser().build().parseSignedClaims(token);

        // Map<String, Object> -> Payload object or JSON
        //String email = (String) jwsClaims.getPayload().get("email");
        //List<Role> roles = (List<Role>) jwsClaims.getPayload().get("roles");
        //Date createdAt = (Date) jwsClaims.getPayload().get("createdAt");

        return SessionStatus.ACTIVE;
    }

    public boolean updatePassword(String token, String email, String oldPassword, String newPassword) throws UserNotFound {

        if(validate(token) != SessionStatus.ACTIVE){
            return false;
        }

        // Get the authenticated user's ID
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(() -> new UserNotFound("User not found")));;

        if(userOptional.isEmpty()){
            return false;
        }

        // Verify the old password
        if (!bCryptPasswordEncoder.matches(oldPassword, userOptional.get().getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Encode and update the new password
        userOptional.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(userOptional.get());

        return true;
    }

    public void resetPassword(String email) throws UserNotFound {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFound(email));

        // Generate a new temporary password
        String tempPassword = generateRandomPassword();

        // Encode the new password and update the user
        user.setPassword(bCryptPasswordEncoder.encode(tempPassword));
        userRepository.save(user);

        // Send email with the new password
        String emailBody = "<p>Your password has been reset. Your new temporary password is: " + tempPassword +
                "\nPlease log in and change your password as soon as possible.</p>";
        try {
            Context context = new Context();
            context.setVariable("tempPassword", tempPassword);
            emailService.sendHtmlEmail(user.getEmail(), "Password reset", emailBody, "new-password", context);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
