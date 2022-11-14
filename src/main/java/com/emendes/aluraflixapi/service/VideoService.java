package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VideoService {

  Page<VideoResponse> findAll(Pageable pageable);

  VideoResponse findById(long id);

  Page<VideoResponse> findByTitle(String title, Pageable pageable);

  VideoResponse create(VideoRequest videoRequest);

  VideoResponse update(long id, VideoRequest videoRequest);

  void deleteById(long id);

  List<VideoResponse> fetchFirstFive();
}
