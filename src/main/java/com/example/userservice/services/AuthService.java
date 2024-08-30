package com.example.userservice.services;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.exceptions.PasswordMismatch;
import com.example.userservice.exceptions.SessionNotFound;
import com.example.userservice.exceptions.UserNotFound;
import com.example.userservice.models.Role;
import com.example.userservice.models.Session;
import com.example.userservice.models.SessionStatus;
import com.example.userservice.models.User;
import com.example.userservice.repository.SessionRepository;
import com.example.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.util.*;

@Service("AuthService")
public class AuthService {

    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
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

    public UserDTO signup(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        return UserDTO.from(savedUser);
    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty()){
            return null;
        }

        Session session = sessionOptional.get();
        if(!session.getSessionStatus().equals(SessionStatus.ACTIVE)) {
            return SessionStatus.ENDED;
        }

        Date currentTime = new Date();
        if(session.getExpiry().before(currentTime)){
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
}
