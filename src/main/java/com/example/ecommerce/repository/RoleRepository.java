package com.example.ecommerce.repository;

import com.example.ecommerce.enums.Roles;
import com.example.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(Roles roleName);
}
