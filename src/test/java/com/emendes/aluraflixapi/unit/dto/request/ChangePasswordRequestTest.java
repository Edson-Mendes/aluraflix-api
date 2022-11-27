package com.emendes.aluraflixapi.unit.dto.request;

import com.emendes.aluraflixapi.dto.request.ChangePasswordRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@DisplayName("Unit tests for ChangePasswordRequest")
class ChangePasswordRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private final String VALID_OLD_PASSWORD = "11223344";
  private final String VALID_NEW_PASSWORD = "12345678";
  private final String VALID_CONFIRM_PASSWORD = "12345678";

  @Nested
  @DisplayName("Tests for oldPassword validation")
  class OldPasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"123456", "f1387hd937hd8e3hd8y"})
    @DisplayName("Validate oldPassword must not return violations when oldPassword is valid")
    void validateOldPassword_MustNotReturnViolations_WhenOldPasswordIsValid(String oldPassword) {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(oldPassword, VALID_NEW_PASSWORD, VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "oldPassword");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate oldPassword must return violations when oldPassword is null")
    void validateOldPassword_MustReturnViolations_WhenOldPasswordIsNull() {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(null, VALID_NEW_PASSWORD, VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "oldPassword");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("oldPassword must not be null or blank");
    }

    @Test
    @DisplayName("Validate oldPassword must return violations when oldPassword is empty")
    void validateOldPassword_MustReturnViolations_WhenOldPasswordIsEmpty() {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest("", VALID_NEW_PASSWORD, VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "oldPassword");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("oldPassword must not be null or blank");
    }

    @Test
    @DisplayName("Validate oldPassword must return violations when oldPassword is blank")
    void validateOldPassword_MustReturnViolations_WhenOldPasswordIsBlank() {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest("   ", VALID_NEW_PASSWORD, VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "oldPassword");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("oldPassword must not be null or blank");
    }

  }

  @Nested
  @DisplayName("Tests for newPassword validation")
  class NewPasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"123456", "f1387hd937hd8e3hd8y"})
    @DisplayName("Validate newPassword must not return violations when newPassword is valid")
    void validateOldPassword_MustNotReturnViolations_WhenOldPasswordIsValid(String newPassword) {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD, newPassword, VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "newPassword");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate newPassword must return violations when newPassword is null")
    void validateOldPassword_MustReturnViolations_WhenOldPasswordIsNull() {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD, null, VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "newPassword");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("newPassword must not be null or blank");
    }

    @Test
    @DisplayName("Validate newPassword must return violations when newPassword is empty")
    void validateOldPassword_MustReturnViolations_WhenOldPasswordIsEmpty() {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD, "", VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "newPassword");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
      Assertions.assertThat(actualViolations.stream().map(ConstraintViolation::getMessage).toList())
          .contains("newPassword must not be null or blank");
    }

    @Test
    @DisplayName("Validate newPassword must return violations when newPassword is blank")
    void validateOldPassword_MustReturnViolations_WhenOldPasswordIsBlank() {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD , "   ", VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "newPassword");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
      Assertions.assertThat(actualViolations.stream().map(ConstraintViolation::getMessage).toList())
          .contains("newPassword must not be null or blank");
    }

    @Test
    @DisplayName("Validate newPassword must return violations when newPassword size is lesser than 6 characters")
    void validatePassword_MustReturnViolations_WhenPasswordSizeIsLesserThan6Characters() {
      String newPasswordWith5Characters = "12345";
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD , newPasswordWith5Characters, VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "newPassword");

      Assertions.assertThat(newPasswordWith5Characters).hasSize(5);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("newPassword must be between 6 and 25 characters long");
    }

    @Test
    @DisplayName("Validate newPassword must return violations when newPassword size is bigger than 25 characters")
    void validatePassword_MustReturnViolations_WhenPasswordSizeIsBiggerThan25Characters() {
      String newPasswordWith5Characters = "12345678901234567890123456";
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD , newPasswordWith5Characters, VALID_CONFIRM_PASSWORD);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "newPassword");

      Assertions.assertThat(newPasswordWith5Characters).hasSize(26);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("newPassword must be between 6 and 25 characters long");
    }

  }

  @Nested
  @DisplayName("Tests for confirmPassword validation")
  class ConfirmPasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"123456", "f1387hd937hd8e3hd8y"})
    @DisplayName("Validate confirmPassword must not return violations when confirmPassword is valid")
    void validateConfirmPassword_MustNotReturnViolations_WhenConfirmPasswordIsValid(String confirmPassword) {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD, VALID_NEW_PASSWORD, confirmPassword);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "confirmPassword");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate confirmPassword must return violations when confirmPassword is null")
    void validateConfirmPassword_MustReturnViolations_WhenConfirmPasswordIsNull() {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD, VALID_NEW_PASSWORD, null);

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "confirmPassword");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("confirmPassword must not be null or blank");
    }

    @Test
    @DisplayName("Validate confirmPassword must return violations when confirmPassword is empty")
    void validateConfirmPassword_MustReturnViolations_WhenConfirmPasswordIsEmpty() {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD, VALID_NEW_PASSWORD, "");

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "confirmPassword");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("confirmPassword must not be null or blank");
    }

    @Test
    @DisplayName("Validate confirmPassword must return violations when confirmPassword is blank")
    void validateConfirmPassword_MustReturnViolations_WhenConfirmPasswordIsBlank() {
      ChangePasswordRequest changePasswordRequest =
          new ChangePasswordRequest(VALID_OLD_PASSWORD, VALID_NEW_PASSWORD, "   ");

      Set<ConstraintViolation<ChangePasswordRequest>> actualViolations = validator.validateProperty(
          changePasswordRequest, "confirmPassword");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("confirmPassword must not be null or blank");
    }

  }

}