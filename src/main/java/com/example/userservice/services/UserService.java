package com.example.userservice.services;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.exceptions.UserNotFound;
import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service("UserService")
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;

    UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    public UserDTO getUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(UserDTO::from).orElse(null);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserDTO setUserRoles(Long userID, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userID);
        List<Role> roles = roleRepository.findAllById(roleIds);

        if (userOptional.isEmpty()) {
           return null;
        }

        User user = userOptional.get();
        user.setRoles(Set.copyOf(roles));

        User savedUser = userRepository.save(user);

        return UserDTO.from(savedUser);
    }
}
