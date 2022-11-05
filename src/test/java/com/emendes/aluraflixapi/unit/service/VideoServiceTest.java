package com.emendes.aluraflixapi.unit.service;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.CategoryNotFoundException;
import com.emendes.aluraflixapi.exception.VideoNotFoundException;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.model.entity.Video;
import com.emendes.aluraflixapi.repository.VideoRepository;
import com.emendes.aluraflixapi.service.CategoryService;
import com.emendes.aluraflixapi.service.VideoService;
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

//  TODO: Pesquisar/Estudar boas práticas em mock comportamento de classes!
@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for VideoService")
class VideoServiceTest {

  @InjectMocks
  private VideoService videoService;
  @Mock
  private VideoRepository videoRepositoryMock;
  @Mock
  private CategoryService categoryServiceMock;
  @Mock
  private ModelMapper mapperMock;

  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

  //  Mocks de algumas dependências
  @BeforeEach
  void setUp() {
    long nonExistentId = 9999L;
    Video video = VideoCreator.withAllParameters();
    Video videoWithoutId = VideoCreator.withoutId();

    BDDMockito.when(mapperMock.map(video, VideoResponse.class))
        .thenReturn(VideoResponseCreator.fromVideo(video));

    BDDMockito.when(videoRepositoryMock.findById(1000L)).thenReturn(Optional.of(video));

    BDDMockito.when(mapperMock
            .map(new VideoRequest("title xpto", "description xpto", "http://www.sitexpto.com", null),
                Video.class))
        .thenReturn(videoWithoutId);

    BDDMockito.when(videoRepositoryMock.save(ArgumentMatchers.any(Video.class))).thenReturn(video);

    BDDMockito.willThrow(new VideoNotFoundException("Video not found for id: " + nonExistentId))
        .given(videoRepositoryMock).findById(nonExistentId);

    BDDMockito.when(categoryServiceMock.existsEnabledCategoryWithId(100)).thenReturn(true);
  }

  @Nested
  @DisplayName("Tests for findAll method")
  class FindAllMethod {

    @Test
    @DisplayName("findAll must return Page<VideoResponse> when Found successfully")
    void findAll_MustReturnPageVideoResponse_WhenFoundSuccessfully() {
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
      BDDMockito.when(videoRepositoryMock.findByTitleIgnoreCaseContaining("xpto", DEFAULT_PAGEABLE)) // Mock comportamento.
          .thenReturn(VideoCreator.videosPage(DEFAULT_PAGEABLE));

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
      BDDMockito.when(categoryServiceMock.existsEnabledCategoryWithId(999)).thenReturn(false);
      VideoRequest videoRequest = new VideoRequest(
          "title xpto", "description xpto", "http://www.sitexpto.com", 999);

//      VideoResponse actualVideoResponse = videoService.create(videoRequest);

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
      VideoRequest videoRequest = new VideoRequest(
          "title xpto updated", "description xpto updated", "http://www.sitexpto.com", 100);
      Video video = VideoCreator.from(1000L, videoRequest);

      BDDMockito.when(videoRepositoryMock.save(video)).thenReturn(video);
      BDDMockito.when(mapperMock.map(video, VideoResponse.class)).thenReturn(VideoResponseCreator.fromVideo(video));

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
      BDDMockito.when(categoryServiceMock.existsEnabledCategoryWithId(1)).thenReturn(true);
      VideoRequest videoRequest = new VideoRequest(
          "title xpto updated", "description xpto updated", "http://www.sitexpto.com", 1);

      Assertions.assertThatExceptionOfType(VideoNotFoundException.class)
          .isThrownBy(() -> videoService.update(9999L, videoRequest))
          .withMessage("Video not found for id: " + 9999L);
    }

    @Test
    @DisplayName("update must throws CategoryNotFoundException when does not exist enabled category with categoryId")
    void update_MustThrowsVideoNotFoundException_WhenDoesNotExistEnabledCategoryWithCategoryId() {
      BDDMockito.when(categoryServiceMock.existsEnabledCategoryWithId(999)).thenReturn(false);
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

}