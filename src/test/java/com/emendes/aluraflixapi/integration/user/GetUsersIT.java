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
@DisplayName("Integration tests for GET /users")
class GetUsersIT {

  @Autowired
  @Qualifier("withoutAuthorizationHeader")
  private TestRestTemplate testRestTemplateNoAuth;

  private final String USERS_URI = "/users";

  @Test
  @Sql(scripts = {"/user/insert.sql", "/user/insert_admin.sql"})
  @DisplayName("get /users must return 200 and Page<UserResponse> when user has role admin")
  void getUsers_MustReturn200AndPageUserResponse_WhenUserHasRoleAdmin(){
    ResponseEntity<PageableResponse<UserResponse>> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("dolor@email.com", "123456")
        .exchange(USERS_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    PageableResponse<UserResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isNotEmpty().hasSize(2);

    List<String> emailList = actualBody.stream().map(UserResponse::getEmail).toList();

    Assertions.assertThat(emailList).contains("lorem@email.com", "dolor@email.com");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql", "/user/insert_admin.sql"})
  @DisplayName("get /users must return 401 and ExceptionDetails when user is not authenticated")
  void getUsers_MustReturn401AndExceptionDetails_WhenUserIsNotAuthenticated() {
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplateNoAuth
        .exchange(USERS_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Unauthorized");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Full authentication is required to access this resource");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql"})
  @DisplayName("get /users must return 403 when user has no role admin")
  void getUsers_MustReturn403_WhenUserHasNoRoleAdmin() {
    ResponseEntity<Void> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("lorem@email.com", "123456")
        .exchange(USERS_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.FORBIDDEN);
  }

  @Test
  @Sql(scripts = {"/user/insert.sql", "/user/insert_admin.sql"})
  @DisplayName("get /users/{id} must return 200 and UserResponse when user has role admin")
  void getUsersId_MustReturn200AndUserResponse_WhenUserHasRoleAdmin() {
    String uri = USERS_URI + "/1";
    ResponseEntity<UserResponse> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("dolor@email.com", "123456")
        .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    UserResponse actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getEmail()).isEqualTo("lorem@email.com");
    Assertions.assertThat(actualBody.getName()).isEqualTo("Lorem Ipsum");
  }

  @Test
  @Sql(scripts = {"/user/insert_admin.sql"})
  @DisplayName("get /users/{id} must return 400 and ExceptionDetails when id is invalid")
  void getUsersId_MustReturn400AndExceptionDetails_WhenIdIsInvalid() {
    String uri = USERS_URI + "/1ooo";

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("dolor@email.com", "123456")
        .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Failed to convert");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/users/1ooo");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql", "/user/insert_admin.sql"})
  @DisplayName("get /users/{id} must return 401 and ExceptionDetails when user is not authenticated")
  void getUsersId_MustReturn401AndExceptionDetails_WhenUserIsNotAuthenticated() {
    String uri = USERS_URI + "/1";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplateNoAuth
        .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Unauthorized");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Full authentication is required to access this resource");
  }

  @Test
  @Sql(scripts = {"/user/insert.sql", "/user/insert_admin.sql"})
  @DisplayName("get /users/{id} must return 403 when user has no role admin")
  void getUsersId_MustReturn403_WhenUserHasNoRoleAdmin() {
    String uri = USERS_URI + "/1000";
    ResponseEntity<Void> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("lorem@email.com", "123456")
        .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.FORBIDDEN);
  }

  @Test
  @Sql(scripts = {"/user/insert_admin.sql"})
  @DisplayName("get /users/{id} must return 404 and ExceptionDetails when user not found")
  void getUsersId_MustReturn404AndExceptionDetails_WhenUserNotFound() {
    String uri = USERS_URI + "/99999";
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("dolor@email.com", "123456")
        .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Resource not found");
    Assertions.assertThat(actualBody.getPath()).isEqualTo("/users/99999");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("User not found for id: 99999");
  }

}
