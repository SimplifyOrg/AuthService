package com.example.userservice.repository;

import com.example.userservice.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long > {
}
