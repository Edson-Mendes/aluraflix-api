package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.CategoryNotFoundException;
import com.emendes.aluraflixapi.exception.VideoNotFoundException;
import com.emendes.aluraflixapi.mapper.VideoMapper;
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
public class VideoServiceImpl implements VideoService{

  private final VideoRepository videoRepository;
  private final CategoryService categoryService;
  private final VideoMapper videoMapper;

  @Override
  public Page<VideoResponse> findAll(Pageable pageable) {
    return videoRepository.findAll(pageable)
        .map(videoMapper::toVideoResponse);
  }

  @Override
  public VideoResponse findById(long id) {
    return videoMapper.toVideoResponse(findVideoById(id));
  }

  @Override
  public Page<VideoResponse> findByTitle(String title, Pageable pageable) {
    return videoRepository.findByTitleIgnoreCaseContaining(title, pageable)
        .map(videoMapper::toVideoResponse);
  }

  @Override
  public VideoResponse create(VideoRequest videoRequest) {
    verifyCategoryIntegrity(videoRequest.getCategoryId());
    Video videoToBeSaved = videoMapper.fromVideoRequest(videoRequest);

    videoToBeSaved.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    return videoMapper.toVideoResponse(videoRepository.save(videoToBeSaved));
  }

  @Override
  public VideoResponse update(long id, VideoRequest videoRequest) {
    verifyCategoryIntegrity(videoRequest.getCategoryId());
    Video videoToBeUpdated = findVideoById(id);

    videoToBeUpdated = videoMapper.merge(videoRequest, videoToBeUpdated);

    return videoMapper.toVideoResponse(videoRepository.save(videoToBeUpdated));
  }

  @Override
  public void deleteById(long id) {
    Video video = findVideoById(id);
    videoRepository.delete(video);
  }

  private Video findVideoById(long id) {
    return videoRepository.findById(id)
        .orElseThrow(() -> new VideoNotFoundException("Video not found for id: " + id));
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
