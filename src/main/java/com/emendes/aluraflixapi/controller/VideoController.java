package com.emendes.aluraflixapi.controller;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/videos")
public class VideoController {

  private final VideoService videoService;

  @GetMapping
  public ResponseEntity<Page<VideoResponse>> findAll(
      @RequestParam(name = "search", required = false) String title,
      @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
    Page<VideoResponse> pageVideoResponse;
    log.info(title);
    if(title == null) {
      pageVideoResponse = videoService.findAll(pageable);
    } else {
      pageVideoResponse = videoService.findByTitle(title, pageable);
    }
    return ResponseEntity.ok(pageVideoResponse);
  }

  @GetMapping("/{id}")
  public ResponseEntity<VideoResponse> findById(@PathVariable(name = "id") long id) {
    return ResponseEntity.ok(videoService.findById(id));
  }

  @PostMapping
  public ResponseEntity<VideoResponse> create(@RequestBody @Valid VideoRequest videoRequest, UriComponentsBuilder uriBuilder) {
    VideoResponse videoResponse = videoService.create(videoRequest);
    URI uri = uriBuilder.path("/videos/{id}").build(videoResponse.getId());
    return ResponseEntity.created(uri).body(videoResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<VideoResponse> update(@PathVariable(name = "id") long id, @RequestBody @Valid VideoRequest videoRequest) {
    return ResponseEntity.ok(videoService.update(id, videoRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) {
    videoService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
