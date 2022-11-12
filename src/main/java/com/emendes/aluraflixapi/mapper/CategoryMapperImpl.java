package com.emendes.aluraflixapi.mapper;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.model.entity.Video;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryMapperImpl implements CategoryMapper {

  private final ModelMapper mapper;

  @Override
  public CategoryResponse toCategoryResponse(Category category) {
    return mapper.map(category, CategoryResponse.class);
  }

  @Override
  public Category fromCategoryRequest(CategoryRequest categoryRequest) {
    return mapper.map(categoryRequest, Category.class);
  }

  @Override
  public Category merge(CategoryRequest source, Category destination) {
    mapper.map(fromCategoryRequest(source), destination);
    return destination;
  }
}
