package com.emendes.aluraflixapi.repository;

import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.model.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

  Page<Video> findByCategory(Category category, Pageable pageable);

  Page<Video> findByTitleIgnoreCaseContaining(String title, Pageable pageable);

  boolean existsByCategory(Category category);

}
