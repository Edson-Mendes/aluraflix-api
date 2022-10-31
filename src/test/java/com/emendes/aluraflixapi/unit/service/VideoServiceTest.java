package com.emendes.aluraflixapi.unit.service;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.VideoNotFoundException;
import com.emendes.aluraflixapi.model.entity.Video;
import com.emendes.aluraflixapi.repository.VideoRepository;
import com.emendes.aluraflixapi.service.VideoService;
import com.emendes.aluraflixapi.util.creator.VideoCreator;
import com.emendes.aluraflixapi.util.creator.VideoResponseCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for VideoService")
class VideoServiceTest {

  @InjectMocks
  private VideoService videoService;
  @Mock
  private VideoRepository videoRepositoryMock;
  @Mock
  private ModelMapper mapperMock;

  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

  @BeforeEach
  void setUp() {
    long nonExistentId = 9999L;
    Video video = VideoCreator.withAllParameters();
    Video videoWithoutId = VideoCreator.withoutId();
    List<Video> videos = List.of(video);
    Page<Video> videosPage = new PageImpl<>(videos, DEFAULT_PAGEABLE, 1);

    BDDMockito.when(videoRepositoryMock.findAll(DEFAULT_PAGEABLE)).thenReturn(videosPage);
    BDDMockito.when(mapperMock.map(video, VideoResponse.class))
        .thenReturn(VideoResponseCreator.fromVideo(video));

    BDDMockito.when(videoRepositoryMock.findById(1000L)).thenReturn(Optional.of(video));

    BDDMockito.when(mapperMock
            .map(new VideoRequest("title xpto", "description xpto", "http://www.sitexpto.com"), Video.class))
        .thenReturn(videoWithoutId);
    BDDMockito.when(videoRepositoryMock.save(videoWithoutId)).thenReturn(video);

    BDDMockito.willThrow(new VideoNotFoundException("Video not found for id: " + nonExistentId))
        .given(videoRepositoryMock).findById(nonExistentId);

  }

  @Nested
  @DisplayName("Tests for findAll method")
  class FindAllMethod {

    @Test
    @DisplayName("findAll must return Page<VideoResponse> when Found successfully")
    void findAll_MustReturnPageVideoResponse_WhenFoundSuccessfully() {
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
      BDDMockito.when(videoRepositoryMock.findAll(DEFAULT_PAGEABLE)).thenReturn(Page.empty(DEFAULT_PAGEABLE));

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
          "description xpto", "http://www.sitexpto.com");

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
  @DisplayName("Tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("create must return VideoResponse when create successfully")
    void create_MustReturnVideoResponse_WhenCreateSuccessfully() {
      VideoRequest videoRequest = new VideoRequest(
          "title xpto", "description xpto", "http://www.sitexpto.com");

      VideoResponse actualVideoResponse = videoService.create(videoRequest);

      Assertions.assertThat(actualVideoResponse).isNotNull();
      Assertions.assertThat(actualVideoResponse.getTitle()).isEqualTo("title xpto");
      Assertions.assertThat(actualVideoResponse.getDescription()).isEqualTo("description xpto");
      Assertions.assertThat(actualVideoResponse.getUrl()).isEqualTo("http://www.sitexpto.com");
    }

  }

  @Nested
  @DisplayName("Tests for update method")
  class UpdateMethod {

    @Test
    @DisplayName("update must return VideoResponse when update successfully")
    void update_MustReturnVideoResponse_WhenUpdateSuccessfully() {
      VideoRequest videoRequest = new VideoRequest(
          "title xpto updated", "description xpto updated", "http://www.sitexpto.com");
      Video video = VideoCreator.from(1000L, videoRequest);
      BDDMockito.when(videoRepositoryMock.save(video)).thenReturn(video);
      BDDMockito.when(mapperMock.map(video, VideoResponse.class)).thenReturn(VideoResponseCreator.fromVideo(video));

      VideoResponse actualVideoResponse = videoService.update(1000L, videoRequest);

      Assertions.assertThat(actualVideoResponse).isNotNull();
      Assertions.assertThat(actualVideoResponse.getTitle()).isEqualTo("title xpto updated");
      Assertions.assertThat(actualVideoResponse.getDescription()).isEqualTo("description xpto updated");
      Assertions.assertThat(actualVideoResponse.getUrl()).isEqualTo("http://www.sitexpto.com");
    }

    @Test
    @DisplayName("update must throws VideoNotFoundException when id does not exist")
    void update_MustThrowsVideoNotFoundException_WhenIdDoesNotExist() {
      VideoRequest videoRequest = new VideoRequest(
          "title xpto updated", "description xpto updated", "http://www.sitexpto.com");

      Assertions.assertThatExceptionOfType(VideoNotFoundException.class)
          .isThrownBy(() -> videoService.update(9999L, videoRequest))
          .withMessage("Video not found for id: " + 9999L);
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