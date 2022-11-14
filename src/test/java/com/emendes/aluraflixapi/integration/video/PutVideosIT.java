package com.emendes.aluraflixapi.integration.video;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import com.emendes.aluraflixapi.dto.response.ValidationExceptionDetails;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@DisplayName("Integration tests for PUT /videos")
class PutVideosIT {

  @Autowired
  @Qualifier("withAuthorizationHeader")
  private TestRestTemplate testRestTemplate;

  private final String VIDEOS_URI_TEMPLATE = "/videos/%s";
  private final VideoRequest VALID_VIDEO_REQUEST = new VideoRequest("Vídeo", "Descrição",
      "http://www.xpto.com/fe23ac5", 1);

  @Test
  @Sql(scripts = {"/video/insert.sql", "/user/insert.sql"})
  @DisplayName("put /videos/{id} must return 200 and VideoResponse when update successfully")
  void putVideosId_MustReturn200AndVideoResponse_WhenUpdateSuccessfully() {
    String uri = String.format(VIDEOS_URI_TEMPLATE, 1);
    VideoRequest videoRequest = new VideoRequest("Vídeo lorem ipsum update",
        "Descrição xpto sobre o vídeo update", "http://www.xpto.com/fe23ac5", 1);
    HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(videoRequest);

    ResponseEntity<VideoResponse> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    VideoResponse actualBody = responseEntity.getBody();

    VideoResponse expectedBody = new VideoResponse(1L, "Vídeo lorem ipsum update",
        "Descrição xpto sobre o vídeo update", "http://www.xpto.com/fe23ac5", 1);

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isEqualTo(expectedBody);
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("put /videos/{id} must return 404 and ExceptionDetails when video does not exist")
  void putVideosId_MustReturn404AndExceptionDetails_WhenVideoDoesNotExist() {
    String uri = String.format(VIDEOS_URI_TEMPLATE, 999);
    HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(VALID_VIDEO_REQUEST);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Resource not found");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(404);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Video not found for id: 999");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/videos/999");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("put /videos/{id} must return 400 and ValidationExceptionDetails when request body has invalid fields")
  void putVideosId_MustReturn400AndValidationExceptionDetails_WhenRequestBodyHasInvalidFields() {
    String uri = String.format(VIDEOS_URI_TEMPLATE, 1);
    VideoRequest videoRequest = new VideoRequest("", null,
        "http://www.xpto.com/fe23ac5", null);
    HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(videoRequest);

    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ValidationExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("title").contains("description").contains("categoryId");
    Assertions.assertThat(actualBody.getMessages()).contains("title must not be null or blank")
        .contains("description must not be null or blank")
        .contains("categoryId must not be null");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("put /videos/{id} must return 400 and ExceptionDetails when id is invalid")
  void putVideosId_MustReturn400AndExceptionDetails_WhenIdIsInvalid() {
    String uri = String.format(VIDEOS_URI_TEMPLATE, "1o");
    HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(VALID_VIDEO_REQUEST);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Failed to convert");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/videos/1o");
  }

  @Test
  @Sql(scripts = {"/category/insert.sql", "/video/insert.sql", "/user/insert.sql"})
  @DisplayName("put /videos/{id} must return 400 and ExceptionDetails when categoryId references deleted Category")
  void putVideosId_MustReturn400AndExceptionDetails_WhenCategoryIdReferencesDeletedCategory() {
    String uri = String.format(VIDEOS_URI_TEMPLATE, 1);
    VideoRequest videoRequest = new VideoRequest("Vídeo lorem ipsum", "Descrição xpto sobre o vídeo",
        "http://www.xpto.com/fe23ac5", 100);
    HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(videoRequest);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Resource not found");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Category not found for id: 100");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/videos/1");
  }

}
