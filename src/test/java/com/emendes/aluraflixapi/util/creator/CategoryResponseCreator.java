package com.emendes.aluraflixapi.util.creator;

import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.model.entity.Category;
import org.modelmapper.ModelMapper;

public class CategoryResponseCreator {
  public static CategoryResponse fromCategory(Category category) {
    ModelMapper mapper = new ModelMapper();
    return mapper.map(category, CategoryResponse.class);
  }
}
