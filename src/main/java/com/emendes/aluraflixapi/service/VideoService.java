package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.VideoNotFoundException;
import com.emendes.aluraflixapi.model.entity.Video;
import com.emendes.aluraflixapi.repository.VideoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class VideoService {

  private final VideoRepository videoRepository;
  private final ModelMapper mapper;

  public Page<VideoResponse> findAll(Pageable pageable) {
    return videoRepository.findAll(pageable)
        .map(v -> mapper.map(v, VideoResponse.class));
  }

  public VideoResponse findById(long id) {
    Video video = videoRepository.findById(id)
        .orElseThrow(() -> new VideoNotFoundException("Video not found for id: "+id));

    return mapper.map(video, VideoResponse.class);
  }

}
