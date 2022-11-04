package com.emendes.aluraflixapi.util.creator;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class CategoryCreator {

  public static Page<Category> categoriesPage(Pageable pageable) {
    List<Category> categories = List.of(withAllParameters());
    return new PageImpl<>(categories, pageable, 1);
  }

  public static Category withAllParameters() {
    LocalDateTime created_at = LocalDateTime.parse("2022-11-03T10:00:00");
    return new Category(100, "Terror xpto", "f1f1f1", created_at, null, true);
  }

  public static Category withoutId() {
    LocalDateTime created_at = LocalDateTime.parse("2022-11-03T10:00:00");
    return new Category(null, "Terror xpto", "f1f1f1", created_at, null, true);
  }

  public static Category from(int id, CategoryRequest categoryRequest) {
    LocalDateTime created_at = LocalDateTime.parse("2022-11-03T10:00:00");
    return new Category(id, categoryRequest.getTitle(), categoryRequest.getColor(), created_at, null, true);
  }
}
