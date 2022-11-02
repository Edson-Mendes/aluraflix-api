package com.emendes.aluraflixapi.util.creator;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.model.entity.Video;

import java.time.LocalDateTime;

public class VideoCreator {
  public static Video withAllParameters() {
    LocalDateTime createdAt = LocalDateTime.parse("2022-10-24T10:00:00");
    return new Video(
        1000L, "title xpto",
        "description xpto", "http://www.sitexpto.com", createdAt, new Category(1));
  }

  public static Video withoutId() {
    LocalDateTime createdAt = LocalDateTime.parse("2022-10-24T10:00:00");
    return new Video(
        null, "title xpto",
        "description xpto", "http://www.sitexpto.com", createdAt, new Category(1));
  }

  public static Video from(long id, VideoRequest videoRequest) {
    LocalDateTime createdAt = LocalDateTime.parse("2022-10-24T10:00:00");
    return new Video(
        id, videoRequest.getTitle(),
        videoRequest.getDescription(), videoRequest.getUrl(), createdAt, new Category(1));
  }
}
