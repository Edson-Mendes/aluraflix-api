package com.emendes.aluraflixapi.repository;

import com.emendes.aluraflixapi.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
