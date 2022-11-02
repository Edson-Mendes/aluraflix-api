package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.VideoNotFoundException;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.model.entity.Video;
import com.emendes.aluraflixapi.repository.VideoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    return mapper.map(findVideoById(id), VideoResponse.class);
  }

  protected Page<VideoResponse> findByCategory(Category category, Pageable pageable) {
    return videoRepository.findByCategory(category, pageable)
        .map(v -> mapper.map(v, VideoResponse.class));
  }

  public VideoResponse create(VideoRequest videoRequest) {
    Video videoToBeSaved = mapper.map(videoRequest, Video.class);
    videoToBeSaved.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

//  TODO: Pensar/pesquisar se é necessário verificar no DB se existe uma categoria com id = 1.
    if (videoToBeSaved.getCategory() == null) {
      videoToBeSaved.setCategory(new Category(1));
    }

    return mapper.map(videoRepository.save(videoToBeSaved), VideoResponse.class);
  }

//  TODO: Criar VideoUpdateRequest para que categoryId seja OBRIGATÓRIO ao ATUALIZAR um vídeo
  public VideoResponse update(long id, VideoRequest videoRequest) {
    Video videoToBeUpdated = findVideoById(id);

    videoToBeUpdated.setTitle(videoRequest.getTitle());
    videoToBeUpdated.setDescription(videoRequest.getDescription());
    videoToBeUpdated.setUrl(videoRequest.getUrl());

    return mapper.map(videoRepository.save(videoToBeUpdated), VideoResponse.class);
  }

  public void deleteById(long id) {
    Video video = findVideoById(id);
    videoRepository.delete(video);
  }

  private Video findVideoById(long id) {
    return videoRepository.findById(id)
        .orElseThrow(() -> new VideoNotFoundException("Video not found for id: " + id));
  }

}
