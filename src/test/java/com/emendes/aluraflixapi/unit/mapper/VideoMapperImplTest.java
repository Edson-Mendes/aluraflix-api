package com.emendes.aluraflixapi.unit.mapper;

import com.emendes.aluraflixapi.config.ModelMapperConfig;
import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.mapper.VideoMapper;
import com.emendes.aluraflixapi.mapper.VideoMapperImpl;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.model.entity.Video;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for VideoMapperImpl")
class VideoMapperImplTest {

  private VideoMapper videoMapper;

  @BeforeEach
  void setUp() {
    videoMapper = new VideoMapperImpl(new ModelMapperConfig().getModelMapper());
  }

  @Test
  @DisplayName("toVideoResponse must return VideoResponse when map successfully")
  void toVideoResponse_MustReturnVideoResponse_WhenMapSuccessfully() {
    Video videoToBeMapped = Video.builder().id(100L).title("Title xpto").description("Description xpto")
        .url("http://www.xpto.com/341e").createdAt(LocalDateTime.parse("2022-11-05T10:00:00"))
        .category(new Category(10))
        .build();

    VideoResponse actualVideoResponse = videoMapper.toVideoResponse(videoToBeMapped);
    VideoResponse expectedVideoResponse = new VideoResponse(
        100L, "Title xpto", "Description xpto", "http://www.xpto.com/341e", 10);

    Assertions.assertThat(actualVideoResponse)
        .isNotNull()
        .isEqualTo(expectedVideoResponse);
  }

  @Test
  @DisplayName("fromVideoRequest must return Video when map successfully")
  void fromVideoRequest_MustReturnVideo_WhenMapSuccessfully() {
    VideoRequest videoRequestToBeMapped = new VideoRequest(
        "Title xpto", "Description xpto", "http://www.xpto.com/341e", 10);

    Video actualVideo = videoMapper.fromVideoRequest(videoRequestToBeMapped);
    Video expectedVideo = Video.builder().title("Title xpto").description("Description xpto")
        .url("http://www.xpto.com/341e").category(new Category(10)).build();

    Assertions.assertThat(actualVideo).isNotNull();
    Assertions.assertThat(actualVideo.getId()).isNull();
    Assertions.assertThat(actualVideo.getTitle()).isEqualTo(expectedVideo.getTitle());
    Assertions.assertThat(actualVideo.getDescription()).isEqualTo(expectedVideo.getDescription());
    Assertions.assertThat(actualVideo.getUrl()).isEqualTo(expectedVideo.getUrl());
    Assertions.assertThat(actualVideo.getCategory()).isNotNull();
    Assertions.assertThat(actualVideo.getCategory().getId()).isEqualTo(expectedVideo.getCategory().getId());
  }

  @Test
  @DisplayName("merge must return Video with VideoRequest values when merge successfully")
  void merge_MustReturnVideoWithVideoRequestValues_WhenMergeSuccessfully() {
    VideoRequest videoRequest = new VideoRequest("New Title xpto", "New Description xpto",
        "http://www.xpto.com/341e", 10);

    Video oldInfoVideo = Video.builder()
        .id(1000L).title("Old Title xpto").description("Old Description xpto")
        .createdAt(LocalDateTime.parse("2022-11-05T10:00:00"))
        .url("http://www.xpto.com/341e").category(new Category(10)).build();


    Video actualVideo = videoMapper.merge(videoRequest, oldInfoVideo);
    Video expectedVideo = Video.builder()
        .id(1000L).title("New Title xpto").description("New Description xpto")
        .createdAt(LocalDateTime.parse("2022-11-05T10:00:00"))
        .url("http://www.xpto.com/341e").category(new Category(10)).build();

    Assertions.assertThat(actualVideo).isNotNull().isEqualTo(expectedVideo);
    Assertions.assertThat(actualVideo.getCategory().getId()).isEqualTo(expectedVideo.getCategory().getId());
  }

}
