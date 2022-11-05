package com.emendes.aluraflixapi.integration.category;

import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@DisplayName("Integration tests for DELETE /categories")
class DeleteCategoriesIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  private final String CATEGORIES_URI_TEMPLATE = "/categories/%s";

  @Test
  @Sql(scripts = {"/category/insert.sql"})
  @DisplayName("delete /categories/{id} must return 204 when delete successfully")
  void deleteCategoriesId_MustReturn204_WhenDeleteSuccessfully() {
    String uri = String.format(CATEGORIES_URI_TEMPLATE, 2);

    ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("delete /categories/{id} must return 404 and ExceptionDetails when category does not exist")
  void deleteCategoriesId_MustReturn404AndExceptionDetails_WhenCategoryDoesNotExist() {
    String uri = String.format(CATEGORIES_URI_TEMPLATE, 999);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Resource not found");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(404);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Category not found for id: 999");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories/999");
  }

  @Test
  @DisplayName("delete /categories/{id} must return 400 and ExceptionDetails when id is invalid")
  void deleteCategoriesId_MustReturn400AndExceptionDetails_WhenIdIsInvalid() {
    String uri = String.format(CATEGORIES_URI_TEMPLATE, "1o");

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Failed to convert");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories/1o");
  }

  @Test
  @DisplayName("delete /categories/{id} must return 400 and ExceptionDetails when try delete category with id 1")
  void deleteCategoriesId_MustReturn400AndExceptionDetails_WhenTryDeleteCategoryWithId1() {
    String uri = String.format(CATEGORIES_URI_TEMPLATE, 1);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Operation not allowed");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Change/delete 'Livre' category not allowed");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories/1");
  }

}
