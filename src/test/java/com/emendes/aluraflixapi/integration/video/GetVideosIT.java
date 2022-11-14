package com.emendes.aluraflixapi.integration.video;

import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.util.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
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
class GetVideosIT {

  @Autowired
  @Qualifier("withAuthorizationHeader")
  private TestRestTemplate testRestTemplate;

  private final String VIDEOS_URI = "/videos";

  @Test
  @Sql(scripts = {"/video/insert.sql", "/user/insert.sql"})
  @DisplayName("get /videos must return 200 and Page<VideoResponse> when found videos successfully")
  void getVideos_MustReturn200AndPageVideoResponse_WhenFoundVideosSuccessfully() {
    ResponseEntity<PageableResponse<VideoResponse>> responseEntity = testRestTemplate.exchange(
        VIDEOS_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    PageableResponse<VideoResponse> actualBody = responseEntity.getBody();

    VideoResponse expectedVideoResponse = new VideoResponse(1L, "Vídeo xpto",
        "Descrição do vídeo xpto", "http://www.xpto.com/f078a", 1);

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isNotEmpty().hasSize(1).contains(expectedVideoResponse);
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("get /videos must return 200 and empty Page when DB has no videos")
  void getVideos_MustReturn200AndEmptyPage_WhenDBHasNoVideos() {
    ResponseEntity<PageableResponse<VideoResponse>> responseEntity = testRestTemplate.exchange(
        VIDEOS_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    PageableResponse<VideoResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isEmpty();
  }

  @Test
  @Sql(scripts = {"/video/insert.sql", "/user/insert.sql"})
  @DisplayName("get /videos?search=xpto must return 200 and Page<VideoResponse> when found videos successfully")
  void getVideosWithParamSearch_MustReturn200AndPageVideoResponse_WhenFoundVideosSuccessfully() {
    String uri = VIDEOS_URI+"?search=xpto";
    ResponseEntity<PageableResponse<VideoResponse>> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    PageableResponse<VideoResponse> actualBody = responseEntity.getBody();

    VideoResponse expectedVideoResponse = new VideoResponse(1L, "Vídeo xpto",
        "Descrição do vídeo xpto", "http://www.xpto.com/f078a", 1);

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isNotEmpty().hasSize(1).contains(expectedVideoResponse);
  }

  @Test
  @Sql(scripts = {"/video/insert.sql", "/user/insert.sql"})
  @DisplayName("get /videos?search=lorem must return 200 and empty Page when DB has no videos with given title")
  void getVideosWithParamSearch_MustReturn200AndEmptyPage_WhenDBHasNoVideoswithGivenTitle() {
    String uri = VIDEOS_URI+"?search=lorem";
    ResponseEntity<PageableResponse<VideoResponse>> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    PageableResponse<VideoResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isEmpty();
  }

  @Test
  @Sql(scripts = {"/video/insert.sql", "/user/insert.sql"})
  @DisplayName("get /videos/{id} must return 200 and VideoResponse when found video successfully")
  void getVideosId_MustReturn200AndPageVideoResponse_WhenFoundSuccessfully() {
    String uri = VIDEOS_URI+"/1";
    ResponseEntity<VideoResponse> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    VideoResponse actualBody = responseEntity.getBody();

    VideoResponse expectedVideoResponse = new VideoResponse(1L, "Vídeo xpto",
        "Descrição do vídeo xpto", "http://www.xpto.com/f078a", 1);

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isEqualTo(expectedVideoResponse);
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("get /videos/{id} must return 404 and ExceptionDetails when not found video")
  void getVideosId_MustReturn404AndExceptionDetails_WhenNotFoundVideo() {
    String uri = VIDEOS_URI+"/1";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Resource not found");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(404);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Video not found for id: 1");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/videos/1");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("get /videos/{id} must return 400 and ExceptionDetails when id is invalid")
  void getVideosId_MustReturn400AndExceptionDetails_WhenIdIsInvalid() {
    String uri = VIDEOS_URI+"/1o";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Failed to convert");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/videos/1o");
  }

}
