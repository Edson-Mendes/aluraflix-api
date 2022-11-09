package com.emendes.aluraflixapi.mapper;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.model.entity.Video;

public interface VideoMapper {

  VideoResponse toVideoResponse(Video video);

  Video fromVideoRequest(VideoRequest videoRequest);

  Video merge(VideoRequest source, Video destination);
}
