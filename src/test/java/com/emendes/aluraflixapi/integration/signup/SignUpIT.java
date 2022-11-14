package com.emendes.aluraflixapi.integration.signup;

import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.dto.response.ValidationExceptionDetails;
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
@DisplayName("Integration tests for GET /categories")
class SignUpIT {

  @Autowired
  @Qualifier("withoutAuthorizationHeader")
  private TestRestTemplate testRestTemplate;

  private final String SIGN_UP_URI = "/auth/signup";

  @Test
  @DisplayName("post /auth/signup must return 201 and UserResponse when create successfully")
  void postAuthSignUp_MustReturn201AndUserResponse_WhenCreateSuccessfully() {
    UserRequest userRequest = new UserRequest("Lorem Ipsum", "lorem@email.com", "123456");
    HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest);

    ResponseEntity<UserResponse> responseEntity = testRestTemplate.exchange(
        SIGN_UP_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    UserResponse actualBody = responseEntity.getBody();

    UserResponse expectedBody = new UserResponse(1L, "Lorem Ipsum", "lorem@email.com");

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
    Assertions.assertThat(actualBody).isNotNull().isEqualTo(expectedBody);
  }

  @Test
  @DisplayName("post /auth/signup must return 400 and ValidationExceptionDetails when request body has invalid fields")
  void postAuthSignUp_MustReturn400AndValidationExceptionDetails_WhenRequestBodyHasInvalidFields() {
    UserRequest userRequest = new UserRequest("", "lorem@", "12345");
    HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest);

    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        SIGN_UP_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ValidationExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("name").contains("email").contains("password");
    Assertions.assertThat(actualBody.getMessages()).contains("name must not be null or blank")
        .contains("email must be a well-formed email address")
        .contains("password must be between 6 and 25 characters long");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("post /auth/signup must return 409 and ExceptionDetails when already exists user with given email")
  void postAuthSignUp_MustReturn409AndExceptionDetails_WhenAlreadyExistsUserWithGivenEmail() {
    UserRequest userRequest = new UserRequest("Dolor Sit", "lorem@email.com", "123456");
    HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        SIGN_UP_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CONFLICT);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Data conflict");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(409);
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/auth/signup");
  }

}
