package com.emendes.aluraflixapi.integration.user;

import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import com.emendes.aluraflixapi.dto.response.UserResponse;
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

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for DELETE /users")
class DeleteUsersIT {

  @Autowired
  @Qualifier("withoutAuthorizationHeader")
  private TestRestTemplate testRestTemplate;

  private final String USERS_URI = "/users";

  @Test
  @Sql(scripts = {"/user/insert.sql", "/user/insert_admin.sql"})
  @DisplayName("delete /users/{id} must return 204 when delete user successfully")
  void deleteUsersId_MustReturn204_WhenDeleteUserSuccessfully() {
    String uri = USERS_URI + "/1";
    ResponseEntity<Void> responseEntity = testRestTemplate
        .withBasicAuth("dolor@email.com", "123456")
        .exchange(uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @Sql(scripts = {"/user/insert_admin.sql"})
  @DisplayName("delete /users/{id} must return 400 and ExceptionDetails when id is invalid")
  void deleteUsersId_MustReturn400AndExceptionDetails_WhenIdIsInvalid() {
    String uri = USERS_URI + "/1ooo";

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate
        .withBasicAuth("dolor@email.com", "123456")
        .exchange(uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Failed to convert");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/users/1ooo");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql", "/user/insert_admin.sql"})
  @DisplayName("delete /users/{id} must return 401 and ExceptionDetails when user is not authenticated")
  void deleteUsersId_MustReturn401AndExceptionDetails_WhenUserIsNotAuthenticated() {
    String uri = USERS_URI + "/1";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate
        .exchange(uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Unauthorized");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Full authentication is required to access this resource");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql", "/user/insert_admin.sql"})
  @DisplayName("delete /users/{id} must return 403 when user has no role admin")
  void deleteUsersId_MustReturn403_WhenUserHasNoRoleAdmin() {
    String uri = USERS_URI + "/1000";
    ResponseEntity<Void> responseEntity = testRestTemplate
        .withBasicAuth("lorem@email.com", "123456")
        .exchange(uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.FORBIDDEN);
  }

  @Test
  @Sql(scripts = {"/user/insert_admin.sql"})
  @DisplayName("delete /users/{id} must return 404 and ExceptionDetails when user not found")
  void deleteUsersId_MustReturn404AndExceptionDetails_WhenUserNotFound() {
    String uri = USERS_URI + "/99999";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate
        .withBasicAuth("dolor@email.com", "123456")
        .exchange(uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Resource not found");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/users/99999");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("User not found for id: 99999");
  }

}
