package com.emendes.aluraflixapi.controller;

import com.emendes.aluraflixapi.controller.swagger.VideoControllerSwagger;
import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.request.groups.CreateInfo;
import com.emendes.aluraflixapi.dto.request.groups.UpdateInfo;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/videos")
public class VideoController implements VideoControllerSwagger {

  private final VideoService videoService;

  @Override
  @GetMapping
  public ResponseEntity<Page<VideoResponse>> findAll(
      @RequestParam(name = "search", required = false) String title,
      @PageableDefault Pageable pageable) {
    Page<VideoResponse> pageVideoResponse;
    if (title == null) {
      pageVideoResponse = videoService.findAll(pageable);
    } else {
      pageVideoResponse = videoService.findByTitle(title, pageable);
    }
    return ResponseEntity.ok(pageVideoResponse);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<VideoResponse> findById(@PathVariable(name = "id") long id) {
    return ResponseEntity.ok(videoService.findById(id));
  }

  @Override
  @GetMapping("/free")
  public ResponseEntity<List<VideoResponse>> fetchFreeSample() {
    return ResponseEntity.ok(videoService.fetchFirstFive());
  }

  @Override
  @PostMapping
  public ResponseEntity<VideoResponse> create(
      @RequestBody @Validated(CreateInfo.class) VideoRequest videoRequest, UriComponentsBuilder uriBuilder) {
//    FIXME: Cheiro de gambiarra! Não tem como ter certeza que a categoria default (Livre) possui id 1.
//    Essa é a solução de momento, se não existir Category com id = 1, CategoryNotFoundException será lançado.
    if (videoRequest.getCategoryId() == null) {
      videoRequest.setCategoryId(1);
    }
    VideoResponse videoResponse = videoService.create(videoRequest);
    URI uri = uriBuilder.path("/videos/{id}").build(videoResponse.getId());
    return ResponseEntity.created(uri).body(videoResponse);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<VideoResponse> update(
      @PathVariable(name = "id") long id,
      @RequestBody @Validated({CreateInfo.class, UpdateInfo.class}) VideoRequest videoRequest) {
    return ResponseEntity.ok(videoService.update(id, videoRequest));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) {
    videoService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
