package com.emendes.aluraflixapi.integration.user;

import com.emendes.aluraflixapi.dto.request.ChangePasswordRequest;
import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
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

import static com.emendes.aluraflixapi.util.constant.SQLPath.INSERT_USER_PATH;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for PUT /users/password")
class PutUsersPasswordIT {

  @Autowired
  @Qualifier("withoutAuthorizationHeader")
  private TestRestTemplate testRestTemplateNoAuth;

  private final String USERS_PASSWORD_URI = "/users/password";

  @Test
  @Sql(scripts = {INSERT_USER_PATH})
  @DisplayName("put /users/password must return 204 when change password successfully")
  void putUsersPassword_MustReturn204_WhenChangePasswordSuccessfully() {
    ChangePasswordRequest requestBody =
        new ChangePasswordRequest("123456", "12345678", "12345678");
    HttpEntity<ChangePasswordRequest> requestEntity = new HttpEntity<>(requestBody);

    ResponseEntity<Void> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("lorem@email.com", "123456")
        .exchange(USERS_PASSWORD_URI, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @Sql(scripts = {INSERT_USER_PATH})
  @DisplayName("put /users/password must return 400 and ValidationExceptionDetails when requestBody is invalid")
  void putUsersPassword_MustReturn400AndValidationExceptionDetails_WhenRequestBodyIsInvalid() {
    ChangePasswordRequest requestBody =
        new ChangePasswordRequest("", "12345678", null);
    HttpEntity<ChangePasswordRequest> requestEntity = new HttpEntity<>(requestBody);

    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("lorem@email.com", "123456")
        .exchange(USERS_PASSWORD_URI, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ValidationExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("oldPassword").contains("confirmPassword");
    Assertions.assertThat(actualBody.getMessages()).contains("oldPassword must not be null or blank")
        .contains("confirmPassword must not be null or blank");
  }

  @Test
  @Sql(scripts = {INSERT_USER_PATH})
  @DisplayName("put /users/password must return 400 and ExceptionDetails when requestBody is invalid")
  void putUsersPassword_MustReturn400AndExceptionDetails_WhenSendWrongOldPassword() {
    ChangePasswordRequest requestBody =
        new ChangePasswordRequest("12345", "12345678", "12345678");
    HttpEntity<ChangePasswordRequest> requestEntity = new HttpEntity<>(requestBody);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("lorem@email.com", "123456")
        .exchange(USERS_PASSWORD_URI, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Something went wrong");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Wrong old password");
  }

  @Test
  @Sql(scripts = {INSERT_USER_PATH})
  @DisplayName("put /users/password must return 400 and ExceptionDetails when newPassword and oldPassword doesn't match")
  void putUsersPassword_MustReturn400AndExceptionDetails_WhenNewPasswordAndOldPasswordDoesntMatch() {
    ChangePasswordRequest requestBody =
        new ChangePasswordRequest("123456", "123456789", "12345678");
    HttpEntity<ChangePasswordRequest> requestEntity = new HttpEntity<>(requestBody);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplateNoAuth
        .withBasicAuth("lorem@email.com", "123456")
        .exchange(USERS_PASSWORD_URI, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Something went wrong");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Passwords do not match");
  }

  @Test
  @DisplayName("put /users/password must return 401 when client is not authenticated")
  void putUsersPassword_MustReturn401_WhenClientIsNotAuthenticated() {
    ChangePasswordRequest requestBody =
        new ChangePasswordRequest("123456", "123456789", "12345678");
    HttpEntity<ChangePasswordRequest> requestEntity = new HttpEntity<>(requestBody);

    ResponseEntity<Void> responseEntity = testRestTemplateNoAuth.exchange(
        USERS_PASSWORD_URI, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

}
