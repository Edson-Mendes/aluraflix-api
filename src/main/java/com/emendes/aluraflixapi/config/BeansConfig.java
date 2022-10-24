package com.emendes.aluraflixapi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

  @Bean
  public ModelMapper getModelMapper() {
    return new ModelMapper();
  }

}
