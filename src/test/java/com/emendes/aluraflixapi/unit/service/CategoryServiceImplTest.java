package com.emendes.aluraflixapi.unit.service;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.CategoryNotFoundException;
import com.emendes.aluraflixapi.exception.OperationNotAllowedException;
import com.emendes.aluraflixapi.mapper.CategoryMapper;
import com.emendes.aluraflixapi.mapper.VideoMapper;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.model.entity.Video;
import com.emendes.aluraflixapi.repository.CategoryRepository;
import com.emendes.aluraflixapi.repository.VideoRepository;
import com.emendes.aluraflixapi.service.CategoryServiceImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for CategoryService")
class CategoryServiceImplTest {

  @InjectMocks
  private CategoryServiceImpl categoryService;
  @Mock
  private CategoryRepository categoryRepositoryMock;
  @Mock
  private CategoryMapper categoryMapperMock;
  @Mock
  private VideoMapper videoMapperMock;
  @Mock
  private VideoRepository videoRepositoryMock;

  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10);

  @BeforeEach
  void setUp() {
    BDDMockito.when(categoryRepositoryMock.findById(100))
        .thenReturn(optionalCategory());
    BDDMockito.willThrow(new CategoryNotFoundException("Category not found for id: " + 999))
        .given(categoryRepositoryMock).findById(999);
    BDDMockito.when(categoryMapperMock.toCategoryResponse(ArgumentMatchers.any(Category.class)))
        .thenReturn(categoryResponse());
  }

  @Nested
  @DisplayName("Tests for findAll method")
  class FindAllMethod {

    @Test
    @DisplayName("findAll must return Page<CategoryResponse> when Found successfully")
    void findAll_MustReturnPageCategoryResponse_WhenFoundSuccessfully() {
      BDDMockito.when(categoryRepositoryMock.findByEnabled(DEFAULT_PAGEABLE, true)) // Mock comportamento.
          .thenReturn(categoriesPage());
      BDDMockito.when(categoryMapperMock.toCategoryResponse(ArgumentMatchers.any(Category.class)))
          .thenReturn(categoryResponse());

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
      BDDMockito.when(categoryRepositoryMock.findByEnabled(DEFAULT_PAGEABLE, true)) // Mock comportamento.
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
      BDDMockito.when(categoryRepositoryMock.findById(100))
          .thenReturn(optionalCategory());

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
      BDDMockito.when(videoRepositoryMock.findByCategory(category(), DEFAULT_PAGEABLE))
          .thenReturn(videosPage());
      BDDMockito.when(videoMapperMock.toVideoResponse(ArgumentMatchers.any(Video.class))).thenReturn(videoResponse());

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
      BDDMockito.when(categoryMapperMock.fromCategoryRequest(categoryRequest())).thenReturn(category());
      BDDMockito.when(categoryRepositoryMock.save(ArgumentMatchers.any(Category.class)))
          .thenReturn(category());

      CategoryRequest categoryRequest = new CategoryRequest("Terror xpto", "f1f1f1");

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
      BDDMockito.when(categoryMapperMock.merge(ArgumentMatchers.any(CategoryRequest.class), ArgumentMatchers.any(Category.class)))
          .thenReturn(updatedCategory());
      BDDMockito.when(categoryRepositoryMock.save(ArgumentMatchers.any(Category.class))).thenReturn(updatedCategory());
      BDDMockito.when(categoryMapperMock.toCategoryResponse(ArgumentMatchers.any(Category.class)))
          .thenReturn(updatedCategoryResponse());

      CategoryRequest categoryRequest = new CategoryRequest("New Terror xpto", "f0f0f0");

      CategoryResponse actualCategoryResponse = categoryService.update(100, categoryRequest);

      Assertions.assertThat(actualCategoryResponse).isNotNull();
      Assertions.assertThat(actualCategoryResponse.getTitle()).isEqualTo("New Terror xpto");
      Assertions.assertThat(actualCategoryResponse.getColor()).isEqualTo("f0f0f0");
    }

    @Test
    @DisplayName("update must throws CategoryNotFoundException when id does not exist")
    void update_MustThrowsCategoryNotFoundException_WhenIdDoesNotExist() {
      CategoryRequest categoryRequest = new CategoryRequest("Terror xpto updated", "f1f1f1");

      Assertions.assertThatExceptionOfType(CategoryNotFoundException.class)
          .isThrownBy(() -> categoryService.update(999, categoryRequest))
          .withMessage("Category not found for id: " + 999);
    }

    @Test
    @DisplayName("update must throws OperationNotAllowedException when try update category with id 1")
    void update_MustThrowsOperationNotAllowedException_WhenTryUpdateCategoryWithId1() {
      CategoryRequest categoryRequest = new CategoryRequest("Terror xpto", "f1f1f1");

      Assertions.assertThatExceptionOfType(OperationNotAllowedException.class)
          .isThrownBy(() -> categoryService.update(1, categoryRequest))
          .withMessage("Change/delete 'Livre' category not allowed");
    }

  }

  @Nested
  @DisplayName("Tests for delete method")
  class DeleteMethod {

    @Test
    @DisplayName("delete must throws CategoryNotFoundException when id does not exist")
    void delete_MustThrowsCategoryNotFoundException_WhenIdDoesNotExist() {
      Assertions.assertThatExceptionOfType(CategoryNotFoundException.class)
          .isThrownBy(() -> categoryService.delete(999))
          .withMessage("Category not found for id: " + 999);
    }

    @Test
    @DisplayName("delete must throws OperationNotAllowedException when try delete category with id 1")
    void delete_MustThrowsOperationNotAllowedException_WhenTryDeleteCategoryWithId1() {
      Assertions.assertThatExceptionOfType(OperationNotAllowedException.class)
          .isThrownBy(() -> categoryService.delete(1))
          .withMessage("Change/delete 'Livre' category not allowed");
    }

    @Test
    @DisplayName("delete must throws OperationNotAllowedException when category has associated videos")
    void delete_MustThrowsOperationNotAllowedException_WhenCategoryHasAssociatedVideos() {
      BDDMockito.willThrow(new OperationNotAllowedException("This category has associated videos"))
          .given(videoRepositoryMock).existsByCategory(ArgumentMatchers.any(Category.class));

      Assertions.assertThatExceptionOfType(OperationNotAllowedException.class)
          .isThrownBy(() -> categoryService.delete(100))
          .withMessage("This category has associated videos");
    }

  }

  public Category category() {
    LocalDateTime created_at = LocalDateTime.parse("2022-11-03T10:00:00");
    return new Category(100, "Terror xpto", "f1f1f1", created_at, null, true);
  }

  private Page<Category> categoriesPage() {
    List<Category> categories = List.of(category());
    return new PageImpl<>(categories, DEFAULT_PAGEABLE, 1);
  }

  private CategoryRequest categoryRequest() {
    return new CategoryRequest("Terror xpto", "f1f1f1");
  }

  private CategoryResponse categoryResponse() {
    return new CategoryResponse(100, "Terror xpto", "f1f1f1");
  }

  private Optional<Category> optionalCategory() {
    return Optional.of(category());
  }

  private Category updatedCategory() {
    LocalDateTime created_at = LocalDateTime.parse("2022-11-03T10:00:00");
    return new Category(100, "New Terror xpto", "f0f0f0", created_at, null, true);
  }

  private CategoryResponse updatedCategoryResponse() {
    return new CategoryResponse(100, "New Terror xpto", "f0f0f0");
  }

  private Page<Video> videosPage() {
    LocalDateTime createdAt = LocalDateTime.parse("2022-10-24T10:00:00");
    Video video = new Video(
        1000L, "title xpto",
        "description xpto", "http://www.sitexpto.com", createdAt, new Category(100));
    List<Video> videos = List.of(video);
    return new PageImpl<>(videos, DEFAULT_PAGEABLE, 1);
  }

  private VideoResponse videoResponse() {
    return new VideoResponse(
        1000L, "title xpto",
        "description xpto", "http://www.sitexpto.com", 100);
  }

}