package com.emendes.aluraflixapi.controller;

import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public Page<CategoryResponse> findAll(@PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
    return categoryService.findAll(pageable);
  }

}
