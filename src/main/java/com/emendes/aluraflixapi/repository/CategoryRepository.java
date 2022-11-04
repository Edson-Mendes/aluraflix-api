package com.emendes.aluraflixapi.repository;

import com.emendes.aluraflixapi.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  /**
   * Busca Categoria por id, e que esteja ativo (enabled = true).
   * @param id da Category a ser encontrada.
   */
  @Query("SELECT c FROM Category c WHERE c.id = :id AND c.enabled = true")
  Optional<Category> findById(@Param("id") int id);

  boolean existsByIdAndEnabled(Integer id, boolean enabled);
}
