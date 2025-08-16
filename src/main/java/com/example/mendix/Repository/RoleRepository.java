package com.example.mendix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mendix.Entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // must return Optional<Role>
}
