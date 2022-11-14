package com.emendes.aluraflixapi.integration.category;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import com.emendes.aluraflixapi.dto.response.ValidationExceptionDetails;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
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
@DisplayName("Integration tests for POST /categories")
class PostCategoriesIT {

  @Autowired
  @Qualifier("withAuthorizationHeader")
  private TestRestTemplate testRestTemplate;

  private final String CATEGORIES_URI = "/categories";

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("post /categories must return 201 and CategoryResponse when create successfully")
  void postCategories_MustReturn201AndCategoryResponse_WhenCreateSuccessfully() {
    CategoryRequest categoryRequest = new CategoryRequest("Sad xpto", "808080");
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(categoryRequest);

    ResponseEntity<CategoryResponse> responseEntity = testRestTemplate.exchange(
        CATEGORIES_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    CategoryResponse actualBody = responseEntity.getBody();

    CategoryResponse expectedBody = new CategoryResponse(2, "Sad xpto", "808080");

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
    Assertions.assertThat(actualBody).isNotNull().isEqualTo(expectedBody);
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("post /categories must return 400 and ValidationExceptionDetails when request body has invalid fields")
  void postCategories_MustReturn400AndValidationExceptionDetails_WhenRequestBodyHasInvalidFields() {
    CategoryRequest categoryRequest = new CategoryRequest("", "8080gg0");
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(categoryRequest);

    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        CATEGORIES_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ValidationExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("title").contains("color");
    Assertions.assertThat(actualBody.getMessages()).contains("title must not be null or blank")
        .contains("color must be a valid hexadecimal")
        .contains("color must be 6 characters long");
  }

  @Test
  @Sql(scripts = {"/category/insert.sql", "/user/insert.sql"})
  @DisplayName("post /categories must return 409 and ExceptionDetails when already exists category with given title")
  void postCategories_MustReturn409AndExceptionDetails_WhenAlreadyExistsCategoryWithGivenTitle() {
    CategoryRequest categoryRequest = new CategoryRequest("Terror", "808080");
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(categoryRequest);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        CATEGORIES_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CONFLICT);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Data conflict");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(409);
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/categories");
  }

}
