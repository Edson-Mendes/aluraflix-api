package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

  Page<CategoryResponse> findAll(Pageable pageable);

  CategoryResponse findById(int id);

  Page<VideoResponse> findVideosByCategoryId(int id, Pageable pageable);

  CategoryResponse create(CategoryRequest categoryRequest);

  CategoryResponse update(int id, CategoryRequest categoryRequest);

  void delete(int id);

  boolean existsEnabledCategoryWithId(Integer id);

}
