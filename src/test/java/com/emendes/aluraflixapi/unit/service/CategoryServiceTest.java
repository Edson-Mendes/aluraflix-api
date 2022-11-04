package com.emendes.aluraflixapi.unit.service;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.CategoryNotFoundException;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.repository.CategoryRepository;
import com.emendes.aluraflixapi.repository.VideoRepository;
import com.emendes.aluraflixapi.service.CategoryService;
import com.emendes.aluraflixapi.util.creator.CategoryCreator;
import com.emendes.aluraflixapi.util.creator.CategoryResponseCreator;
import com.emendes.aluraflixapi.util.creator.VideoCreator;
import com.emendes.aluraflixapi.util.creator.VideoResponseCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for CategoryService")
class CategoryServiceTest {

  @InjectMocks
  private CategoryService categoryService;
  @Mock
  private CategoryRepository categoryRepositoryMock;
  @Mock
  private ModelMapper mapperMock;
  @Mock
  private VideoRepository videoRepositoryMock;

  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "title");

  @BeforeEach
  void setUp() {
    int nonExistentId = 999;

    BDDMockito.when(categoryRepositoryMock.findById(100)).thenReturn(Optional.of(CategoryCreator.withAllParameters()));

    BDDMockito.willThrow(new CategoryNotFoundException("Category not found for id: " + nonExistentId))
        .given(categoryRepositoryMock).findById(nonExistentId);

    BDDMockito.when(mapperMock.map(CategoryCreator.withAllParameters(), CategoryResponse.class))
        .thenReturn(CategoryResponseCreator.fromCategory(CategoryCreator.withAllParameters()));

    BDDMockito.when(videoRepositoryMock.findByCategory(CategoryCreator.withAllParameters(), DEFAULT_PAGEABLE))
        .thenReturn(VideoCreator.videosPage(DEFAULT_PAGEABLE));
  }

  @Nested
  @DisplayName("Tests for findAll method")
  class FindAllMethod {

    @Test
    @DisplayName("findAll must return Page<CategoryResponse> when Found successfully")
    void findAll_MustReturnPageCategoryResponse_WhenFoundSuccessfully() {
      BDDMockito.when(categoryRepositoryMock.findAll(DEFAULT_PAGEABLE)) // Mock comportamento.
          .thenReturn(CategoryCreator.categoriesPage(DEFAULT_PAGEABLE));

      Page<CategoryResponse> actualCategoryResponsePage = categoryService.findAll(DEFAULT_PAGEABLE);

      CategoryResponse expectedCategoryResponse = new CategoryResponse(100, "Terror xpto", "f1f1f1");
      Assertions.assertThat(actualCategoryResponsePage)
          .isNotEmpty()
          .hasSize(1)
          .contains(expectedCategoryResponse);
    }

    @Test
    @DisplayName("findAll must return empty Page when there are no categoriess")
    void findAll_MustReturnsEmptyPage_WhenThereAreNoCategories() {
      BDDMockito.when(categoryRepositoryMock.findAll(DEFAULT_PAGEABLE)) // Mock comportamento.
          .thenReturn(Page.empty(DEFAULT_PAGEABLE));

      Page<CategoryResponse> actualCategoryResponsePage = categoryService.findAll(DEFAULT_PAGEABLE);

      Assertions.assertThat(actualCategoryResponsePage)
          .isEmpty();
    }

  }

  @Nested
  @DisplayName("Tests for findById method")
  class FindByIdMethod {

    @Test
    @DisplayName("findById must return CategoryResponse when found by id successfully")
    void findById_MustReturnCategoryResponse_WhenFoundByIdSuccessfully() {
      CategoryResponse actualCategoryResponse = categoryService.findById(100);

      CategoryResponse expectedCategoryResponse = new CategoryResponse(100, "Terror xpto", "f1f1f1");

      Assertions.assertThat(actualCategoryResponse)
          .isNotNull().isEqualTo(expectedCategoryResponse);
    }

    @Test
    @DisplayName("findById must throws CategoryNotFoundException when not found by id")
    void findById_MustThrowsCategoryNotFoundException_WhenNotFoundById() {
      Assertions.assertThatExceptionOfType(CategoryNotFoundException.class)
          .isThrownBy(() -> categoryService.findById(999))
          .withMessage("Category not found for id: " + 999);
    }

  }

  @Nested
  @DisplayName("Tests for findVideosByCategoryId method")
  class FindVideosByCategoryIdMethod {

    @Test
    @DisplayName("findVideosByCategoryId must return Page<VideoResponse> when found videos by categoryId successfully")
    void findVideosByCategoryId_MustReturnPageVideoResponse_WhenFoundVideosByCategoryIdSuccessfully() {
      BDDMockito.when(mapperMock.map(VideoCreator.withAllParameters(), VideoResponse.class))
          .thenReturn(VideoResponseCreator.fromVideo(VideoCreator.withAllParameters()));
      Page<VideoResponse> actualVideoResponsePage = categoryService.findVideosByCategoryId(100, DEFAULT_PAGEABLE);

      VideoResponse expectedVideoResponse = new VideoResponse(
          1000L, "title xpto", "description xpto", "http://www.sitexpto.com", 100);

      Assertions.assertThat(actualVideoResponsePage)
          .isNotEmpty()
          .hasSize(1)
          .contains(expectedVideoResponse);
    }

    @Test
    @DisplayName("findVideosByCategoryId must throws CategoryNotFoundException when not found categoryId")
    void findVideosByCategoryId_MustThrowsCategoryNotFoundException_WhenNotFoundCategoryId() {
      Assertions.assertThatExceptionOfType(CategoryNotFoundException.class)
          .isThrownBy(() -> categoryService.findVideosByCategoryId(999, DEFAULT_PAGEABLE))
          .withMessage("Category not found for id: " + 999);
    }

  }

  @Nested
  @DisplayName("Tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("create must return CategoryResponse when create successfully")
    void create_MustReturnCategoryResponse_WhenCreateSuccessfully() {
      CategoryRequest categoryRequest = new CategoryRequest("Terror xpto", "f1f1f1");

      BDDMockito.when(mapperMock.map(categoryRequest, Category.class))
          .thenReturn(CategoryCreator.withoutId());
      BDDMockito.when(categoryRepositoryMock.save(ArgumentMatchers.any(Category.class)))
          .thenReturn(CategoryCreator.withAllParameters());

      CategoryResponse actualCategoryResponse = categoryService.create(categoryRequest);

      Assertions.assertThat(actualCategoryResponse).isNotNull();
      Assertions.assertThat(actualCategoryResponse.getTitle()).isEqualTo("Terror xpto");
      Assertions.assertThat(actualCategoryResponse.getColor()).isEqualTo("f1f1f1");
    }

  }

  @Nested
  @DisplayName("Tests for update method")
  class UpdateMethod {

    @Test
    @DisplayName("update must return CategoryResponse when update successfully")
    void update_MustReturnCategoryResponse_WhenUpdateSuccessfully() {
      CategoryRequest categoryRequest = new CategoryRequest("Terror xpto updated", "f1f1f1");
      Category category = CategoryCreator.from(200, categoryRequest);

      BDDMockito.when(categoryRepositoryMock.save(ArgumentMatchers.any(Category.class))).thenReturn(category);
      BDDMockito.when(mapperMock.map(category, CategoryResponse.class)).thenReturn(CategoryResponseCreator.fromCategory(category));

      CategoryResponse actualCategoryResponse = categoryService.update(100, categoryRequest);

      Assertions.assertThat(actualCategoryResponse).isNotNull();
      Assertions.assertThat(actualCategoryResponse.getTitle()).isEqualTo("Terror xpto updated");
      Assertions.assertThat(actualCategoryResponse.getColor()).isEqualTo("f1f1f1");
    }

    @Test
    @DisplayName("update must throws CategoryNotFoundException when id does not exist")
    void update_MustThrowsCategoryNotFoundException_WhenIdDoesNotExist() {
      CategoryRequest categoryRequest = new CategoryRequest("Terror xpto updated", "f1f1f1");

      Assertions.assertThatExceptionOfType(CategoryNotFoundException.class)
          .isThrownBy(() -> categoryService.update(999, categoryRequest))
          .withMessage("Category not found for id: " + 999);
    }

  }

  @Nested
  @DisplayName("Tests for delete method")
  class DeleteMethod {

    @Test
    @DisplayName("deleteById must throws CategoryNotFoundException when id does not exist")
    void deleteById_MustThrowsCategoryNotFoundException_WhenIdDoesNotExist() {
      Assertions.assertThatExceptionOfType(CategoryNotFoundException.class)
          .isThrownBy(() -> categoryService.delete(999))
          .withMessage("Category not found for id: " + 999);
    }

  }

}