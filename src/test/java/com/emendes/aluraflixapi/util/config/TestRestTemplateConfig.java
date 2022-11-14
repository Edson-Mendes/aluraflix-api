package com.emendes.aluraflixapi.util.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@Configuration
@Lazy
@Profile("test")
public class TestRestTemplateConfig {

  @Value("${local.server.port}")
  private int port;

  @Bean(name = "withAuthorizationHeader")
  public TestRestTemplate testRestTemplateWithAuthorizationHeader() {
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
        .rootUri("http://localhost:"+port)
        .basicAuthentication("lorem@email.com", "123456");
    return new TestRestTemplate(restTemplateBuilder);
  }

  @Bean(name = "withoutAuthorizationHeader")
  public TestRestTemplate testRestTemplate() {
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
        .rootUri("http://localhost:"+port);
    return new TestRestTemplate(restTemplateBuilder);
  }

}
