package com.emendes.aluraflixapi.mapper;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.model.entity.Category;

public interface CategoryMapper {

  CategoryResponse toCategoryResponse(Category category);

  Category fromCategoryRequest(CategoryRequest categoryRequest);

  Category merge(CategoryRequest source, Category destination);

}
