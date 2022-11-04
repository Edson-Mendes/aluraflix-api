package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.CategoryNotFoundException;
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

  private final CategoryService categoryService;
  private final ModelMapper mapper;

  public Page<VideoResponse> findAll(Pageable pageable) {
    return videoRepository.findAll(pageable)
        .map(v -> mapper.map(v, VideoResponse.class));
  }

  public VideoResponse findById(long id) {
    return mapper.map(findVideoById(id), VideoResponse.class);
  }

  public Page<VideoResponse> findByTitle(String title, Pageable pageable) {
    return videoRepository.findByTitleIgnoreCaseContaining(title, pageable)
        .map(v -> mapper.map(v, VideoResponse.class));
  }

  public VideoResponse create(VideoRequest videoRequest) {
    if (!categoryService.existsEnabledCategoryWithId(videoRequest.getCategoryId())) {
      throw new CategoryNotFoundException("Category not found for id: " + videoRequest.getCategoryId());
    }

    Video videoToBeSaved = mapper.map(videoRequest, Video.class);
    videoToBeSaved.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    return mapper.map(videoRepository.save(videoToBeSaved), VideoResponse.class);
  }

//  TODO: Impedir do cliente atualizar um vídeo com uma categoria deletada
  public VideoResponse update(long id, VideoRequest videoRequest) {
    Video videoToBeUpdated = findVideoById(id);

    videoToBeUpdated.setTitle(videoRequest.getTitle());
    videoToBeUpdated.setDescription(videoRequest.getDescription());
    videoToBeUpdated.setUrl(videoRequest.getUrl());
    videoToBeUpdated.setCategory(new Category(videoRequest.getCategoryId()));

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
