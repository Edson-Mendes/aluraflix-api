package com.emendes.aluraflixapi.integration.category;

import com.emendes.aluraflixapi.dto.response.CategoryResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for GET /categories")
class GetCategoriesIT {

  @Autowired
  @Qualifier("withAuthorizationHeader")
  private TestRestTemplate testRestTemplate;
  @Autowired
  @Qualifier("withoutAuthorizationHeader")
  private TestRestTemplate testRestTemplateNoAuth;

  private final String CATEGORIES_URI = "/categories";

  @Test
  @Sql(scripts = {"/category/insert.sql", "/user/insert.sql"})
  @DisplayName("get /categories must return 200 and Page<CategoryResponse> when found only enable categories")
  void getCategories_MustReturn200AndPageCategoryResponse_WhenFoundCategoriesSuccessfully() {
    ResponseEntity<PageableResponse<CategoryResponse>> responseEntity = testRestTemplate
        .exchange(CATEGORIES_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    PageableResponse<CategoryResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isNotEmpty().hasSize(2);

    List<String> titleList = actualBody.stream().map(CategoryResponse::getTitle).toList();

    Assertions.assertThat(titleList).contains("Livre", "Terror");
  }

  @Test
  @Sql(scripts = {"/category/insert.sql", "/user/insert.sql"})
  @DisplayName("get /categories/{id} must return 200 and CategoryResponse when found category successfully")
  void getCategoriesId_MustReturn200AndPageCategoryResponse_WhenFoundSuccessfully() {
    String uri = CATEGORIES_URI + "/2";
    ResponseEntity<CategoryResponse> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    CategoryResponse actualBody = responseEntity.getBody();

    CategoryResponse expectedCategoryResponse = new CategoryResponse(2, "Terror", "808080");

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isEqualTo(expectedCategoryResponse);
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("get /categories/{id} must return 404 and ExceptionDetails when not found category")
  void getCategoriesId_MustReturn404AndExceptionDetails_WhenNotFoundCategory() {
    String uri = CATEGORIES_URI + "/100";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Resource not found");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(404);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Category not found for id: 100");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories/100");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("get /categories/{id} must return 400 and ExceptionDetails when id is invalid")
  void getCategoriesId_MustReturn400AndExceptionDetails_WhenIdIsInvalid() {
    String uri = CATEGORIES_URI + "/1o";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Failed to convert");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories/1o");
  }

  @Test
  @Sql(scripts = {"/video/insert.sql", "/user/insert.sql"})
  @DisplayName("get /categories/{id}/videos must return 200 and Page<VideosResponse> when found Videos successfully")
  void getCategoriesIdVideos_MustReturn200AndPageVideoResponse_WhenFoundVideosSuccessfully() {
    String uri = CATEGORIES_URI + "/1/videos";
    ResponseEntity<PageableResponse<VideoResponse>> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    PageableResponse<VideoResponse> actualBody = responseEntity.getBody();
    VideoResponse expectedVideoResponse = new VideoResponse(1L, "Vídeo xpto",
        "Descrição do vídeo xpto", "http://www.xpto.com/f078a", 1);

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isNotEmpty().hasSize(1).contains(expectedVideoResponse);
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("get /categories/{id}/videos must return 200 and empty Page when not found Videos with given categoryId")
  void getCategoriesIdVideos_MustReturn200AndEmptyPage_WhenNotFoundVideosWithGivenCategoryId() {
    String uri = CATEGORIES_URI + "/1/videos";
    ResponseEntity<PageableResponse<VideoResponse>> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    PageableResponse<VideoResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isEmpty();
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("get /categories/{id}/videos must return 404 and ExceptionDetails when not found category")
  void getCategoriesIdVideos_MustReturn404AndExceptionDetails_WhenNotFoundCategory() {
    String uri = CATEGORIES_URI + "/100/videos";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Resource not found");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(404);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Category not found for id: 100");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories/100/videos");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("get /categories/{id}/videos must return 400 and ExceptionDetails when id is invalid")
  void getCategoriesIdVideos_MustReturn400AndExceptionDetails_WhenIdIsInvalid() {
    String uri = CATEGORIES_URI + "/1oo/videos";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Failed to convert");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories/1oo/videos");
  }

  @Test
  @DisplayName("get /categories must return 401 when client is not authenticated")
  void getCategories_MustReturn401_WhenClientIsNotAuthenticated() {
    ResponseEntity<Void> responseEntity = testRestTemplateNoAuth.exchange(
        CATEGORIES_URI, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("get /categories/{id} must return 401 when client is not authenticated")
  void getCategoriesId_MustReturn401_WhenClientIsNotAuthenticated() {
    String uri = CATEGORIES_URI + "/1";

    ResponseEntity<Void> responseEntity = testRestTemplateNoAuth.exchange(
        uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("get /categories/{id}/videos must return 401 when client is not authenticated")
  void getCategoriesIdVideos_MustReturn401_WhenClientIsNotAuthenticated() {
    String uri = CATEGORIES_URI + "/1/videos";

    ResponseEntity<Void> responseEntity = testRestTemplateNoAuth.exchange(
        uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

}
