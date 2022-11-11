package com.emendes.aluraflixapi.config;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.model.entity.Video;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper getModelMapper() {
    ModelMapper mapper = new ModelMapper();

    PropertyMap<VideoRequest, Video> videoMap = new PropertyMap<>() {
      protected void configure() {
        map().setId(null);
      }
    };

    mapper.addMappings(videoMap);

    return mapper;
  }

}
