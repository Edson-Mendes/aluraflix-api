package com.emendes.aluraflixapi.repository;

import com.emendes.aluraflixapi.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

  Optional<Role> findByName(String name);

}
