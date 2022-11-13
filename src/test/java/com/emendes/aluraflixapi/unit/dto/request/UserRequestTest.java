package com.emendes.aluraflixapi.unit.dto.request;

import com.emendes.aluraflixapi.dto.request.UserRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@DisplayName("Unit tests for UserRequest")
class UserRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  private final String VALID_NAME = "Lorem Ipsum";
  private final String VALID_EMAIL = "lorem@email.com";
  private final String VALID_PASSWORD = "123456";

  @Nested
  @DisplayName("Tests for name validation")
  class NameValidation {

    @ParameterizedTest
    @ValueSource(strings = {"Lorem", "I"})
    @DisplayName("Validate name must not return violations when name is valid")
    void validateName_MustNotReturnViolations_WhenNameIsValid(String name) {
      UserRequest userRequest = new UserRequest(name, VALID_EMAIL, VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "name");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate name must return violations when name is null")
    void validateName_MustReturnViolations_WhenNameIsNull() {
      UserRequest userRequest = new UserRequest(null, VALID_EMAIL, VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "name");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("name must not be null or blank");
    }

    @Test
    @DisplayName("Validate name must return violations when name is empty")
    void validateName_MustReturnViolations_WhenNameIsEmpty() {
      UserRequest userRequest = new UserRequest("", VALID_EMAIL, VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "name");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("name must not be null or blank");
    }

    @Test
    @DisplayName("Validate name must return violations when name is blank")
    void validateName_MustReturnViolations_WhenNameIsBlank() {
      UserRequest userRequest = new UserRequest("   ", VALID_EMAIL, VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "name");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("name must not be null or blank");
    }

    @Test
    @DisplayName("Validate name must return violations when name size is bigger than 100 characters")
    void validateName_MustReturnViolations_WhenNameSizeIsBiggerThan100Characters() {
      String nameWith101Characters =
          "name xpto name xpto name xpto name xpto name xpto name xpto name xpto name xpto name xpto name xpto..";
      UserRequest userRequest = new UserRequest(nameWith101Characters, VALID_EMAIL, VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "name");

      Assertions.assertThat(nameWith101Characters).hasSize(101);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("name must be maximum 100 characters long");
    }

  }

  @Nested
  @DisplayName("Tests for email validation")
  class EmailValidation {

    @ParameterizedTest
    @ValueSource(strings = {"lorem@email.com", "dolor.set@email.com.br"})
    @DisplayName("Validate email must not return violations when email is valid")
    void validateEmail_MustNotReturnViolations_WhenEmailIsValid(String email) {
      UserRequest userRequest = new UserRequest(VALID_NAME, email, VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "email");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate email must return violations when email is null")
    void validateEmail_MustReturnViolations_WhenEmailIsNull() {
      UserRequest userRequest = new UserRequest(VALID_NAME, null, VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "email");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("email must not be null or blank");
    }

    @Test
    @DisplayName("Validate email must return violations when email is empty")
    void validateEmail_MustReturnViolations_WhenEmailIsEmpty() {
      UserRequest userRequest = new UserRequest(VALID_NAME, "", VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "email");
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualMessages)
          .contains("email must not be null or blank");
    }

    @Test
    @DisplayName("Validate email must return violations when email is blank")
    void validateEmail_MustReturnViolations_WhenEmailIsBlank() {
      UserRequest userRequest = new UserRequest(VALID_NAME, "   ", VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "email");
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
      Assertions.assertThat(actualMessages)
          .contains("email must not be null or blank");
    }

    @Test
    @DisplayName("Validate email must return violations when email size is bigger than 255 characters")
    void validateEmail_MustReturnViolations_WhenEmailSizeIsBiggerThan255Characters() {
      String nameWith101Characters = "loremloremloremloremloremloremloremloremloremlorem"+
          "loremloremloremloremloremloremloremloremloremlorem"+
          "@emailemailemailemailemailemailemailemailemailemail"+
          "emailemailemailemailemailemailemailemailemailemail"+
          "emailemailemailemailemailemailemailemailemailemail"+
          ".comm";
      UserRequest userRequest = new UserRequest(VALID_NAME, nameWith101Characters, VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "email");
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(nameWith101Characters).hasSize(256);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
      Assertions.assertThat(actualMessages)
          .contains("email must be 255 characters long");
    }

    @ParameterizedTest
    @ValueSource(strings = {"lorememail.com", "@email.com", "lorem@"})
    @DisplayName("Validate email must return violations when email is invalid")
    void validateEmail_MustReturnViolations_WhenEmailIsInvalid(String email) {
      UserRequest userRequest = new UserRequest(VALID_NAME, email, VALID_PASSWORD);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "email");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("email must be a well-formed email address");
    }

  }

  @Nested
  @DisplayName("Tests for password validation")
  class PasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"123456", "@de3$d916rg917gd7%$976dte"})
    @DisplayName("Validate password must not return violations when password is valid")
    void validatePassword_MustNotReturnViolations_WhenPasswordIsValid(String password) {
      UserRequest userRequest = new UserRequest(VALID_NAME, VALID_EMAIL, password);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "password");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate password must return violations when password is null")
    void validatePassword_MustReturnViolations_WhenPasswordIsNull() {
      UserRequest userRequest = new UserRequest(VALID_NAME, VALID_EMAIL, null);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "password");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("password must not be null or blank");
    }

    @Test
    @DisplayName("Validate password must return violations when password is empty")
    void validatePassword_MustReturnViolations_WhenPasswordIsEmpty() {
      UserRequest userRequest = new UserRequest(VALID_NAME, VALID_EMAIL, "");

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "password");
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
      Assertions.assertThat(actualMessages)
          .contains("password must not be null or blank");
    }

    @Test
    @DisplayName("Validate password must return violations when password is blank")
    void validatePassword_MustReturnViolations_WhenPasswordIsBlank() {
      UserRequest userRequest = new UserRequest(VALID_NAME, VALID_EMAIL, "       ");

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "password");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("password must not be null or blank");
    }

    @Test
    @DisplayName("Validate password must return violations when password size is lesser than 6 characters")
    void validatePassword_MustReturnViolations_WhenPasswordSizeIsLesserThan6Characters() {
      String passwordWith5Characters = "12345";
      UserRequest userRequest = new UserRequest(VALID_NAME, VALID_EMAIL, passwordWith5Characters);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "password");

      Assertions.assertThat(passwordWith5Characters).hasSize(5);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("password must be between 6 and 25 characters long");
    }

    @Test
    @DisplayName("Validate password must return violations when password size is bigger than 25 characters")
    void validatePassword_MustReturnViolations_WhenPasswordSizeIsBiggerThan25Characters() {
      String passwordWith5Characters = "12345678901234567890123456";
      UserRequest userRequest = new UserRequest(VALID_NAME, VALID_EMAIL, passwordWith5Characters);

      Set<ConstraintViolation<UserRequest>> actualViolations = validator.validateProperty(
          userRequest, "password");

      Assertions.assertThat(passwordWith5Characters).hasSize(26);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("password must be between 6 and 25 characters long");
    }

  }

}