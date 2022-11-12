package com.emendes.aluraflixapi.repository;

import com.emendes.aluraflixapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
