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
import org.springframework.http.HttpStatus;
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
    verifyCategoryIntegrity(videoRequest.getCategoryId());
    Video videoToBeSaved = videoRequestToVideo(videoRequest);

    return mapper.map(videoRepository.save(videoToBeSaved), VideoResponse.class);
  }

  public VideoResponse update(long id, VideoRequest videoRequest) {
    verifyCategoryIntegrity(videoRequest.getCategoryId());
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

  private Video videoRequestToVideo(VideoRequest videoRequest) {
    return Video.builder()
        .id(null)
        .title(videoRequest.getTitle())
        .description(videoRequest.getDescription())
        .url(videoRequest.getUrl())
        .category(new Category(videoRequest.getCategoryId()))
        .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .build();
  }

  /**
   * Verifica se o {@code id} informado pertence a uma Category existente e ativa.
   * @param categoryId id da Category a ser verificada.
   * @throws CategoryNotFoundException se {@code id} não pertence a uma Category existente e ativa.
   */
  private void verifyCategoryIntegrity(Integer categoryId) {
    if (!categoryService.existsEnabledCategoryWithId(categoryId)) {
      throw new CategoryNotFoundException("Category not found for id: " + categoryId, HttpStatus.BAD_REQUEST);
    }
  }

}
