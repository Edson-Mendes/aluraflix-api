package com.emendes.aluraflixapi.unit.service;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.CategoryNotFoundException;
import com.emendes.aluraflixapi.exception.VideoNotFoundException;
import com.emendes.aluraflixapi.mapper.VideoMapper;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.model.entity.Video;
import com.emendes.aluraflixapi.repository.VideoRepository;
import com.emendes.aluraflixapi.service.CategoryServiceImpl;
import com.emendes.aluraflixapi.service.VideoServiceImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for VideoService")
class VideoServiceImplTest {

  @InjectMocks
  private VideoServiceImpl videoService;
  @Mock
  private VideoRepository videoRepositoryMock;
  @Mock
  private CategoryServiceImpl categoryService;
  @Mock
  private VideoMapper mapperMock;

  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10);

  //  Mocks comuns entre testes
  @BeforeEach
  void setUp() {
    BDDMockito.willThrow(new VideoNotFoundException("Video not found for id: " + 9999L))
        .given(videoRepositoryMock).findById(9999L);
    BDDMockito.when(videoRepositoryMock.findById(1000L)).thenReturn(optionalVideo());
  }

  @Nested
  @DisplayName("Tests for findAll method")
  class FindAllMethod {

    @Test
    @DisplayName("findAll must return Page<VideoResponse> when Found successfully")
    void findAll_MustReturnPageVideoResponse_WhenFoundSuccessfully() {
      BDDMockito.when(mapperMock.toVideoResponse(ArgumentMatchers.any(Video.class))).thenReturn(videoResponse());
      BDDMockito.when(videoRepositoryMock.findAll(DEFAULT_PAGEABLE)) // Mock comportamento.
          .thenReturn(VideoCreator.videosPage(DEFAULT_PAGEABLE));

      Page<VideoResponse> actualVideoResponsePage = videoService.findAll(DEFAULT_PAGEABLE);

      VideoResponse expectedVideoResponse = VideoResponseCreator.fromVideo(VideoCreator.withAllParameters());
      Assertions.assertThat(actualVideoResponsePage)
          .isNotEmpty()
          .hasSize(1)
          .contains(expectedVideoResponse);
    }

    @Test
    @DisplayName("findAll must return empty Page when there are no videos")
    void findAll_MustReturnsEmptyPage_WhenThereAreNoVideos() {
      BDDMockito.when(videoRepositoryMock.findAll(DEFAULT_PAGEABLE)) // Mock comportamento.
          .thenReturn(Page.empty(DEFAULT_PAGEABLE));

      Page<VideoResponse> actualVideoResponsePage = videoService.findAll(DEFAULT_PAGEABLE);

      Assertions.assertThat(actualVideoResponsePage)
          .isEmpty();
    }

  }

  @Nested
  @DisplayName("Tests for findById method")
  class FindByIdMethod {

    @Test
    @DisplayName("findById must return VideoResponse when found by id successfully")
    void findById_MustReturnVideoResponse_WhenFoundByIdSuccessfully() {
      BDDMockito.when(mapperMock.toVideoResponse(ArgumentMatchers.any(Video.class))).thenReturn(videoResponse());

      VideoResponse actualVideoResponse = videoService.findById(1000L);

      VideoResponse expectedVideoResponse = new VideoResponse(
          1000L, "title xpto",
          "description xpto", "http://www.sitexpto.com", 100);

      Assertions.assertThat(actualVideoResponse)
          .isNotNull().isEqualTo(expectedVideoResponse);
    }

    @Test
    @DisplayName("findById must throws VideoNotFoundException when not found by id")
    void findById_MustThrowsVideoNotFoundException_WhenNotFoundById() {
      Assertions.assertThatExceptionOfType(VideoNotFoundException.class)
          .isThrownBy(() -> videoService.findById(9999L))
          .withMessage("Video not found for id: " + 9999L);
    }

  }

  @Nested
  @DisplayName("Tests for findByTitle method")
  class FindByTitleMethod {

    @Test
    @DisplayName("findByTitle must return Page<VideoResponse> when Found successfully")
    void findByTitle_MustReturnPageVideoResponse_WhenFoundSuccessfully() {
      BDDMockito.when(mapperMock.toVideoResponse(ArgumentMatchers.any(Video.class))).thenReturn(videoResponse());
      BDDMockito.when(videoRepositoryMock.findByTitleIgnoreCaseContaining("xpto", DEFAULT_PAGEABLE)) // Mock comportamento.
          .thenReturn(videosPage());

      Page<VideoResponse> actualVideoResponsePage = videoService.findByTitle("xpto", DEFAULT_PAGEABLE);

      VideoResponse expectedVideoResponse = new VideoResponse(
          1000L, "title xpto", "description xpto", "http://www.sitexpto.com", 100);
      Assertions.assertThat(actualVideoResponsePage)
          .isNotEmpty()
          .hasSize(1)
          .contains(expectedVideoResponse);
    }

    @Test
    @DisplayName("findByTitle must return empty Page when there are no videos")
    void findByTitle_MustReturnsEmptyPage_WhenThereAreNoVideos() {
      BDDMockito.when(videoRepositoryMock.findByTitleIgnoreCaseContaining("xalala", DEFAULT_PAGEABLE)) // Mock comportamento.
          .thenReturn(Page.empty(DEFAULT_PAGEABLE));

      Page<VideoResponse> actualVideoResponsePage = videoService.findByTitle("xalala", DEFAULT_PAGEABLE);

      Assertions.assertThat(actualVideoResponsePage)
          .isEmpty();
    }

  }

  @Nested
  @DisplayName("Tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("create must return VideoResponse when create successfully")
    void create_MustReturnVideoResponse_WhenCreateSuccessfully() {
      BDDMockito.when(categoryService.existsEnabledCategoryWithId(100)).thenReturn(true);
      BDDMockito.when(mapperMock.fromVideoRequest(videoRequest())).thenReturn(videoWithoutIdAndCreatedAt());
      BDDMockito.when(videoRepositoryMock.save(ArgumentMatchers.any(Video.class))).thenReturn(video());
      BDDMockito.when(mapperMock.toVideoResponse(ArgumentMatchers.any(Video.class))).thenReturn(videoResponse());

      VideoRequest videoRequest = new VideoRequest(
          "title xpto", "description xpto", "http://www.sitexpto.com", 100);

      VideoResponse actualVideoResponse = videoService.create(videoRequest);

      Assertions.assertThat(actualVideoResponse).isNotNull();
      Assertions.assertThat(actualVideoResponse.getTitle()).isEqualTo("title xpto");
      Assertions.assertThat(actualVideoResponse.getDescription()).isEqualTo("description xpto");
      Assertions.assertThat(actualVideoResponse.getUrl()).isEqualTo("http://www.sitexpto.com");
      Assertions.assertThat(actualVideoResponse.getCategoryId()).isEqualTo(100);
    }

    @Test
    @DisplayName("create must throws CategoryNotFoundException when does not exist enabled category with categoryId")
    void create_MustThrowsCategoryNotFoundException_WhenDoesNotExistEnabledCategoryWithCategoryId() {
      BDDMockito.when(categoryService.existsEnabledCategoryWithId(999)).thenReturn(false);
      VideoRequest videoRequest = new VideoRequest(
          "title xpto", "description xpto", "http://www.sitexpto.com", 999);

      Assertions.assertThatExceptionOfType(CategoryNotFoundException.class)
          .isThrownBy(() -> videoService.create(videoRequest))
          .withMessage("Category not found for id: 999");
    }
  }

  @Nested
  @DisplayName("Tests for update method")
  class UpdateMethod {

    @Test
    @DisplayName("update must return VideoResponse when update successfully")
    void update_MustReturnVideoResponse_WhenUpdateSuccessfully() {
      BDDMockito.when(categoryService.existsEnabledCategoryWithId(100)).thenReturn(true);
      BDDMockito.when(mapperMock.merge(ArgumentMatchers.any(VideoRequest.class), ArgumentMatchers.any(Video.class)))
          .thenReturn(updatedVideo());
      BDDMockito.when(mapperMock.toVideoResponse(ArgumentMatchers.any(Video.class))).thenReturn(updatedVideoResponse());
      BDDMockito.when(videoRepositoryMock.save(ArgumentMatchers.any(Video.class))).thenReturn(updatedVideo());

      VideoRequest videoRequest = new VideoRequest(
          "title xpto updated", "description xpto updated", "http://www.sitexpto.com", 100);
      Video video = VideoCreator.from(1000L, videoRequest);

      VideoResponse actualVideoResponse = videoService.update(1000L, videoRequest);

      Assertions.assertThat(actualVideoResponse).isNotNull();
      Assertions.assertThat(actualVideoResponse.getTitle()).isEqualTo("title xpto updated");
      Assertions.assertThat(actualVideoResponse.getDescription()).isEqualTo("description xpto updated");
      Assertions.assertThat(actualVideoResponse.getUrl()).isEqualTo("http://www.sitexpto.com");
      Assertions.assertThat(actualVideoResponse.getCategoryId()).isEqualTo(100);
    }

    @Test
    @DisplayName("update must throws VideoNotFoundException when id does not exist")
    void update_MustThrowsVideoNotFoundException_WhenIdDoesNotExist() {
      BDDMockito.when(categoryService.existsEnabledCategoryWithId(100)).thenReturn(true);

      VideoRequest videoRequest = new VideoRequest(
          "title xpto updated", "description xpto updated", "http://www.sitexpto.com", 100);

      Assertions.assertThatExceptionOfType(VideoNotFoundException.class)
          .isThrownBy(() -> videoService.update(9999L, videoRequest))
          .withMessage("Video not found for id: " + 9999L);
    }

    @Test
    @DisplayName("update must throws CategoryNotFoundException when does not exist enabled category with categoryId")
    void update_MustThrowsCategoryNotFoundException_WhenDoesNotExistEnabledCategoryWithCategoryId() {
      BDDMockito.when(categoryService.existsEnabledCategoryWithId(999)).thenReturn(false);
      VideoRequest videoRequest = new VideoRequest(
          "title xpto updated", "description xpto updated", "http://www.sitexpto.com", 999);

      Assertions.assertThatExceptionOfType(CategoryNotFoundException.class)
          .isThrownBy(() -> videoService.update(1000L, videoRequest))
          .withMessage("Category not found for id: " + 999);
    }

  }

  @Nested
  @DisplayName("Tests for deleteById method")
  class DeleteByIdMethod {

    @Test
    @DisplayName("deleteById must throws VideoNotFoundException when id does not exist")
    void deleteById_MustThrowsVideoNotFoundException_WhenIdDoesNotExist() {
      Assertions.assertThatExceptionOfType(VideoNotFoundException.class)
          .isThrownBy(() -> videoService.deleteById(9999L))
          .withMessage("Video not found for id: " + 9999L);
    }

  }

  private Video video() {
    LocalDateTime createdAt = LocalDateTime.parse("2022-10-24T10:00:00");
    return new Video(
        1000L, "title xpto",
        "description xpto", "http://www.sitexpto.com", createdAt, new Category(100));
  }

  private Video videoWithoutIdAndCreatedAt() {
    return new Video(null, "title xpto", "description xpto",
        "http://www.sitexpto.com", null, new Category(100));
  }

  private Video updatedVideo() {
    LocalDateTime createdAt = LocalDateTime.parse("2022-10-24T10:00:00");
    return new Video(
        1000L, "title xpto updated",
        "description xpto updated", "http://www.sitexpto.com", createdAt, new Category(100));
  }

  private VideoRequest videoRequest() {
    return new VideoRequest("title xpto", "description xpto", "http://www.sitexpto.com", 100);
  }

  private VideoResponse videoResponse() {
    return new VideoResponse(
        1000L, "title xpto",
        "description xpto", "http://www.sitexpto.com", 100);
  }

  private VideoResponse updatedVideoResponse() {
    return new VideoResponse(
        1000L, "title xpto updated",
        "description xpto updated", "http://www.sitexpto.com", 100);
  }

  private Optional<Video> optionalVideo() {
    return Optional.of(video());
  }

  private Page<Video> videosPage() {
    List<Video> videos = List.of(video());
    return new PageImpl<>(videos, DEFAULT_PAGEABLE, 1);
  }

}