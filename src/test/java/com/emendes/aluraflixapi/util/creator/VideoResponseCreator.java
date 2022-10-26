package com.emendes.aluraflixapi.util.creator;

import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.model.entity.Video;
import org.modelmapper.ModelMapper;

public class VideoResponseCreator {
  public static VideoResponse fromVideo(Video video) {
    ModelMapper mapper = new ModelMapper();
    return mapper.map(video, VideoResponse.class);
  }

}
