package com.emendes.aluraflixapi.unit.dto.request;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
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

@DisplayName("Unit tests for CategoryRequest")
class CategoryRequestTest {


  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  private final String VALID_TITLE = "Terror XPTO";
  private final String VALID_COLOR = "A9A9A9";

  @Nested
  @DisplayName("Tests for title validation")
  class TitleValidation {

    @ParameterizedTest
    @ValueSource(strings = {"Terror", "Sad"})
    @DisplayName("Validate title must not return violations when title is valid")
    void validateTitle_MustNotReturnViolations_WhenTitleIsValid(String title) {
      CategoryRequest categoryRequest = new CategoryRequest(title, VALID_COLOR);

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "title");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate title must return violations when title is null")
    void validateTitle_MustReturnViolations_WhenTitleIsNull() {
      CategoryRequest categoryRequest = new CategoryRequest(null, VALID_COLOR);

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "title");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("title must not be null or blank");
    }

    @Test
    @DisplayName("Validate title must return violations when title is empty")
    void validateTitle_MustReturnViolations_WhenTitleIsEmpty() {
      CategoryRequest categoryRequest = new CategoryRequest("", VALID_COLOR);

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "title");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("title must not be null or blank");
    }

    @Test
    @DisplayName("Validate title must return violations when title is blank")
    void validateTitle_MustReturnViolations_WhenTitleIsBlank() {
      CategoryRequest categoryRequest = new CategoryRequest("   ", VALID_COLOR);

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "title");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("title must not be null or blank");
    }

    @Test
    @DisplayName("Validate title must return violations when title size is bigger than 50 characters")
    void validateTitle_MustReturnViolations_WhenTitleSizeIsBiggerThan50Characters() {
      String titleWith51Characters = "title xptotitle xptotitle xptotitle xptotitle xpto.";
      CategoryRequest categoryRequest = new CategoryRequest(titleWith51Characters, VALID_COLOR);

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "title");

      Assertions.assertThat(titleWith51Characters).hasSize(51);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("title must be maximum 50 characters long");
    }

  }

  @Nested
  @DisplayName("Tests for color validation")
  class ColorValidation {

    @ParameterizedTest
    @ValueSource(strings = {"000000", "ffffff", "f1f1f1"})
    @DisplayName("Validate color must not return violations when color is valid")
    void validateColor_MustNotReturnViolations_WhenColorIsValid(String color) {
      CategoryRequest categoryRequest = new CategoryRequest(VALID_TITLE, color);

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "color");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate color must return violations when color is null")
    void validateColor_MustReturnViolations_WhenColorIsNull() {
      CategoryRequest categoryRequest = new CategoryRequest(VALID_TITLE, null);

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "color");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("color must not be null or blank");
    }

    @Test
    @DisplayName("Validate color must return violations when color is empty")
    void validateColor_MustReturnViolations_WhenColorIsEmpty() {
      CategoryRequest categoryRequest = new CategoryRequest(VALID_TITLE, "");

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "color");
      List<String> actualListOfMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
      Assertions.assertThat(actualListOfMessages)
          .contains("color must not be null or blank");
    }

    @Test
    @DisplayName("Validate color must return violations when color is blank")
    void validateColor_MustReturnViolations_WhenColorIsBlank() {
      CategoryRequest categoryRequest = new CategoryRequest(VALID_TITLE, "   ");

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "color");
      List<String> actualListOfMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(3);
      Assertions.assertThat(actualListOfMessages)
          .contains("color must not be null or blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567"})
    @DisplayName("Validate color must return violations when color size is different than 6 characters")
    void validateColor_MustReturnViolations_WhenColorSizeIsDifferentThan6Characters(String color) {
      CategoryRequest categoryRequest = new CategoryRequest(VALID_TITLE, color);

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "color");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("color must be 6 characters long");
    }

    @ParameterizedTest
    @ValueSource(strings = {"00000G", "-10000"})
    @DisplayName("Validate color must return violations when color is not valid hexadecimal")
    void validateColor_MustReturnViolations_WhenColorIsNotValidHexadecimal(String color) {
      CategoryRequest categoryRequest = new CategoryRequest(VALID_TITLE, color);

      Set<ConstraintViolation<CategoryRequest>> actualViolations = validator.validateProperty(
          categoryRequest, "color");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("color must be a valid hexadecimal");
    }

  }

}