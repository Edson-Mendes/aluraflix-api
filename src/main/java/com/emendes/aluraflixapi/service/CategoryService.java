package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.exception.CategoryNotFoundException;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final ModelMapper mapper;

  public Page<CategoryResponse> findAll(Pageable pageable) {
    return categoryRepository.findAll(pageable)
        .map(c -> mapper.map(c, CategoryResponse.class));
  }

  public CategoryResponse findById(int id) {
    return mapper.map(findCategoryById(id), CategoryResponse.class);
  }

  private Category findCategoryById(int id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new CategoryNotFoundException("Video not found for id: " + id));
  }

}
