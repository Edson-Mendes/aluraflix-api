package com.emendes.aluraflixapi.controller;

import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/videos")
public class VideoController {

  private final VideoService videoService;

  @GetMapping
  public Page<VideoResponse> findAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
    return videoService.findAll(pageable);
  }

}
