package com.emendes.aluraflixapi.integration.category;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import com.emendes.aluraflixapi.dto.response.ValidationExceptionDetails;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
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
@DisplayName("Integration tests for PUT /categories")
class PutCategoriesIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  private final String CATEGORIES_URI = "/categories/%s";
  private final CategoryRequest VALID_CATEGORY_REQUEST = new CategoryRequest("Sad XPTO", "c3c3c3");

  @Test
  @Sql(scripts = {"/category/insert.sql"})
  @DisplayName("put /categories/{id} must return 200 and CategoryResponse when update successfully")
  void putCategoriesId_MustReturn200AndCategoryResponse_WhenUpdateSuccessfully() {
    String uri = String.format(CATEGORIES_URI, 2);
    CategoryRequest categoryRequest = new CategoryRequest("Terror XPTO", "808080");
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(categoryRequest);

    ResponseEntity<CategoryResponse> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    CategoryResponse actualBody = responseEntity.getBody();

    CategoryResponse expectedBody = new CategoryResponse(2, "Terror XPTO", "808080");

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isEqualTo(expectedBody);
  }

  @Test
  @DisplayName("put /categories/{id} must return 404 and ExceptionDetails when category does not exist")
  void putCategoriesId_MustReturn404AndExceptionDetails_WhenCategoryDoesNotExist() {
    String uri = String.format(CATEGORIES_URI, 999);
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(VALID_CATEGORY_REQUEST);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

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
  @DisplayName("put /categories/{id} must return 400 and ExceptionDetails when id is invalid")
  void putCategoriesId_MustReturn400AndExceptionDetails_WhenIdIsInvalid() {
    String uri = String.format(CATEGORIES_URI, "1o");
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(VALID_CATEGORY_REQUEST);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Failed to convert");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories/1o");
  }

  @Test
  @Sql(scripts = {"/category/insert.sql"})
  @DisplayName("put /categories/{id} must return 400 and ValidationExceptionDetails when request body has invalid fields")
  void putCategoriesId_MustReturn400AndValidationExceptionDetails_WhenRequestBodyHasInvalidFields() {
    String uri = String.format(CATEGORIES_URI, 2);
    CategoryRequest categoryRequest = new CategoryRequest(null, "c0c0c0gg");
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(categoryRequest);

    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ValidationExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("title").contains("color");
    Assertions.assertThat(actualBody.getMessages()).contains("title must not be null or blank")
        .contains("color must be 6 characters long")
        .contains("color must be a valid hexadecimal");
  }

  @Test
  @DisplayName("put /categories/{id} must return 400 and ExceptionDetails when try update category with id 1")
  void putCategoriesId_MustReturn400AndExceptionDetails_WhenTryUpdateCategoryWithId1() {
    String uri = String.format(CATEGORIES_URI, 1);
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(VALID_CATEGORY_REQUEST);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Operation not allowed");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Change/delete 'Livre' category not allowed");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories/1");
  }

}
