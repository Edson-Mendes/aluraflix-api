package com.emendes.aluraflixapi.controller;

import com.emendes.aluraflixapi.controller.swagger.CategoryControllerSwagger;
import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController implements CategoryControllerSwagger {

  private final CategoryService categoryService;

  @Override
  @GetMapping
//  TODO: Alterar o valor default de size do pageable
//  E alterar nos testes
  public Page<CategoryResponse> findAll(@PageableDefault Pageable pageable) {
    return categoryService.findAll(pageable);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> findById(@PathVariable(name = "id") int id) {
    return ResponseEntity.ok(categoryService.findById(id));
  }

  @Override
  @GetMapping("/{id}/videos")
  public ResponseEntity<Page<VideoResponse>> findVideosByCategory(
      @PathVariable(name = "id") int id, @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(categoryService.findVideosByCategoryId(id, pageable));
  }

  @Override
  @PostMapping
  public ResponseEntity<CategoryResponse> create(
      @RequestBody @Valid CategoryRequest categoryRequest, UriComponentsBuilder uriBuilder) {
    CategoryResponse categoryResponse = categoryService.create(categoryRequest);
    URI uri = uriBuilder.path("/categories/{id}").build(categoryResponse.getId());
    return ResponseEntity.created(uri).body(categoryResponse);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponse> update(
      @PathVariable(name = "id") int id, @RequestBody @Valid CategoryRequest categoryRequest) {
    return ResponseEntity.ok(categoryService.update(id, categoryRequest));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") int id) {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
