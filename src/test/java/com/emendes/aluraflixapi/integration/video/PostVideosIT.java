package com.emendes.aluraflixapi.integration.video;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.ValidationExceptionDetails;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for GET /videos")
class PostVideosIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  private final String VIDEOS_URI = "/videos";

  @Test
  @Sql(scripts = {"/category/insert.sql"})
  @DisplayName("postVideos must return 201 and VideoResponse when create successfully")
  void postVideos_MustReturn201AndVideoResponse_WhenCreateSuccessfully() {
    VideoRequest videoRequest = new VideoRequest("Vídeo lorem ipsum", "Descrição xpto sobre o vídeo",
        "http://www.xpto.com/fe23ac5", 2);
    HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(videoRequest);

    ResponseEntity<VideoResponse> responseEntity = testRestTemplate.exchange(
        VIDEOS_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    VideoResponse actualBody = responseEntity.getBody();

    VideoResponse expectedBody = new VideoResponse(1L, "Vídeo lorem ipsum",
        "Descrição xpto sobre o vídeo", "http://www.xpto.com/fe23ac5", 2);

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
    Assertions.assertThat(actualBody).isNotNull().isEqualTo(expectedBody);
  }

  @Test
  @DisplayName("postVideos must return 201 and VideoResponse when send null categoryId")
  void postVideos_MustReturn201AndVideoResponse_WhenSendNullCategoryId() {
    VideoRequest videoRequest = new VideoRequest("Vídeo lorem ipsum", "Descrição xpto sobre o vídeo",
        "http://www.xpto.com/fe23ac5", null);
    HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(videoRequest);

    ResponseEntity<VideoResponse> responseEntity = testRestTemplate.exchange(
        VIDEOS_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    VideoResponse actualBody = responseEntity.getBody();

    VideoResponse expectedBody = new VideoResponse(1L, "Vídeo lorem ipsum",
        "Descrição xpto sobre o vídeo", "http://www.xpto.com/fe23ac5", 1);

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
    Assertions.assertThat(actualBody).isNotNull().isEqualTo(expectedBody);
  }

  @Test
  @DisplayName("postVideo must return 400 and ValidationExceptionDetails when request body has invalid fields")
  void postVideo_MustReturn400AndValidationExceptionDetails_WhenRequestBodyHasInvalidFields() {
    VideoRequest videoRequest = new VideoRequest("", null,
        "http://www.xpto.com/fe23ac5", 1);
    HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(videoRequest);

    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        VIDEOS_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ValidationExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("title").contains("description");
    Assertions.assertThat(actualBody.getMessages()).contains("title must not be null or blank")
        .contains("description must not be null or blank");
  }

  @Test
  @Sql(scripts = {"/category/insert.sql"})
  @DisplayName("postVideo must return 400 and ExceptionDetails when categoryId references deleted Category")
  void postVideo_MustReturn400AndExceptionDetails_WhenCategoryIdReferencesDeletedCategory() {
    VideoRequest videoRequest = new VideoRequest("Vídeo lorem ipsum", "Descrição xpto sobre o vídeo",
        "http://www.xpto.com/fe23ac5", 100);
    HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(videoRequest);

    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        VIDEOS_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ValidationExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
  }

}
