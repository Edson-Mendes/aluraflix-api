package com.emendes.aluraflixapi.mapper;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.model.entity.Video;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VideoMapperImpl implements VideoMapper {

  private final ModelMapper mapper;

  @Override
  public VideoResponse toVideoResponse(Video video) {
    return mapper.map(video, VideoResponse.class);
  }

  @Override
  public Video fromVideoRequest(VideoRequest videoRequest) {
    return mapper.map(videoRequest, Video.class);
  }

  @Override
  public Video merge(VideoRequest source, Video destination) {
    mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    mapper.map(fromVideoRequest(source), destination);
    return destination;
  }

}
