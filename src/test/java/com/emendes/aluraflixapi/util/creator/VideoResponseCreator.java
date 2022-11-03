package com.emendes.aluraflixapi.util.creator;

import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.model.entity.Video;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class VideoResponseCreator {
  public static VideoResponse fromVideo(Video video) {
    ModelMapper mapper = new ModelMapper();
    return mapper.map(video, VideoResponse.class);
  }

  public static Page<VideoResponse> videoResponsePage(Pageable pageable) {
    VideoResponse videoResponse = new VideoResponse(
        1000L, "title xpto", "description xpto", "http://www.sitexpto.com", 100);
    return new PageImpl(List.of(videoResponse), pageable, 1);
  }

}
