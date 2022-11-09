package com.emendes.aluraflixapi.unit.mapper;

import com.emendes.aluraflixapi.config.ModelMapperConfig;
import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.mapper.CategoryMapper;
import com.emendes.aluraflixapi.mapper.CategoryMapperImpl;
import com.emendes.aluraflixapi.model.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for CategoryMapperImpl")
class CategoryMapperImplTest {

  private CategoryMapper categoryMapper;

  @BeforeEach
  void setUp() {
    categoryMapper = new CategoryMapperImpl(new ModelMapperConfig().getModelMapper());
  }

  @Test
  @DisplayName("toCategoryResponse must return CategoryResponse when map successfully")
  void toCategoryResponse_MustReturnCategoryResponse_WhenMapSuccessfully() {
    Category categoryToBeMapped = Category.builder().id(100).title("Comedy xpto").color("ebf213")
        .createdAt(LocalDateTime.parse("2022-11-05T10:00:00"))
        .enabled(true).build();

    CategoryResponse actualCategoryResponse = categoryMapper.toCategoryResponse(categoryToBeMapped);
    CategoryResponse expectedCategoryResponse = new CategoryResponse(100, "Comedy xpto", "ebf213");

    Assertions.assertThat(actualCategoryResponse)
        .isNotNull()
        .isEqualTo(expectedCategoryResponse);
  }

  @Test
  @DisplayName("fromCategoryRequest must return Category when map successfully")
  void fromCategoryRequest_MustReturnCategory_WhenMapSuccessfully() {
    CategoryRequest categoryRequestToBeMapped = new CategoryRequest("Comedy xpto", "ebf213");

    Category actualCategory = categoryMapper.fromCategoryRequest(categoryRequestToBeMapped);
    Category expectedCategory = Category.builder().title("Comedy xpto").color("ebf213").build();

    Assertions.assertThat(actualCategory).isNotNull();
    Assertions.assertThat(actualCategory.getId()).isNull();
    Assertions.assertThat(actualCategory.getCreatedAt()).isNull();
    Assertions.assertThat(actualCategory.getDeletedAt()).isNull();
    Assertions.assertThat(actualCategory.isEnabled()).isTrue();
    Assertions.assertThat(actualCategory.getTitle()).isEqualTo(expectedCategory.getTitle());
    Assertions.assertThat(actualCategory.getColor()).isEqualTo(expectedCategory.getColor());

  }

  @Test
  @DisplayName("merge must return Category with CategoryRequest values when merge successfully")
  void merge_MustReturnCategoryWithCategoryRequestValues_WhenMergeSuccessfully() {
    CategoryRequest categoryRequest = new CategoryRequest("New Comedy xpto", "753173");

    Category oldInfoCategory = Category.builder()
        .id(100).title("Old Comedy xpto").color("ebf213")
        .createdAt(LocalDateTime.parse("2022-11-05T10:00:00")).enabled(true).build();


    Category actualCategory = categoryMapper.merge(categoryRequest, oldInfoCategory);
    Category expectedCategory = Category.builder()
        .id(100).title("New Comedy xpto").color("753173")
        .createdAt(LocalDateTime.parse("2022-11-05T10:00:00")).enabled(true).build();

    Assertions.assertThat(actualCategory).isNotNull();
    Assertions.assertThat(actualCategory.getId()).isEqualTo(expectedCategory.getId());
    Assertions.assertThat(actualCategory.getCreatedAt()).isEqualTo(expectedCategory.getCreatedAt());
    Assertions.assertThat(actualCategory.getDeletedAt()).isNull();
    Assertions.assertThat(actualCategory.isEnabled()).isTrue();
    Assertions.assertThat(actualCategory.getTitle()).isEqualTo(expectedCategory.getTitle());
    Assertions.assertThat(actualCategory.getColor()).isEqualTo(expectedCategory.getColor());
  }

}
