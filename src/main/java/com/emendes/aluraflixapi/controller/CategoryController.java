package com.emendes.aluraflixapi.controller;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public Page<CategoryResponse> findAll(@PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
    return categoryService.findAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> findById(@PathVariable(name = "id") int id) {
    return ResponseEntity.ok(categoryService.findById(id));
  }

  @GetMapping("/{id}/videos")
  public ResponseEntity<Page<VideoResponse>> findVideosByCategory(
      @PathVariable(name = "id") int id, @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
    return ResponseEntity.ok(categoryService.findVideosByCategoryId(id, pageable));
  }

  @PostMapping
  public ResponseEntity<CategoryResponse> create(
      @RequestBody @Valid CategoryRequest categoryRequest, UriComponentsBuilder uriBuilder) {
    CategoryResponse categoryResponse = categoryService.create(categoryRequest);
    URI uri = uriBuilder.path("/categories/{id}").build(categoryResponse.getId());
    return ResponseEntity.created(uri).body(categoryResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponse> update(
      @PathVariable(name = "id") int id, @RequestBody @Valid CategoryRequest categoryRequest) {
    return ResponseEntity.ok(categoryService.update(id, categoryRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") int id) {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
