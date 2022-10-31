package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.response.CategoryResponse;
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

}
