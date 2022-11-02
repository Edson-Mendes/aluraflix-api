package com.emendes.aluraflixapi.unit.dto.request;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
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

@DisplayName("Unit tests for VideoRequest")
class VideoRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private final String VALID_TITLE = "title xpto do vídeo";
  private final String VALID_DESCRIPTION = "description xpto do vídeo";
  private final String VALID_URL = "http://www.xptovideos.com/loremipsum";

  @Nested
  @DisplayName("Tests for title validation")
  class TitleValidation {

    @ParameterizedTest
    @ValueSource(strings = {"Title XPTO", "T"})
    @DisplayName("Validate title must not return violations when title is valid")
    void validateTitle_MustNotReturnViolations_WhenTitleIsValid(String title) {
      VideoRequest videoRequest = new VideoRequest(title, VALID_DESCRIPTION, VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate title must return violations when title is null")
    void validateTitle_MustReturnViolations_WhenTitleIsNull() {
      VideoRequest videoRequest = new VideoRequest(null, VALID_DESCRIPTION, VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("title must not be null or blank");
    }

    @Test
    @DisplayName("Validate title must return violations when title is empty")
    void validateTitle_MustReturnViolations_WhenTitleIsEmpty() {
      VideoRequest videoRequest = new VideoRequest("", VALID_DESCRIPTION, VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("title must not be null or blank");
    }

    @Test
    @DisplayName("Validate title must return violations when title is blank")
    void validateTitle_MustReturnViolations_WhenTitleIsBlank() {
      VideoRequest videoRequest = new VideoRequest("   ", VALID_DESCRIPTION, VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("title must not be null or blank");
    }

    @Test
    @DisplayName("Validate title must return violations when title size is bigger than 100 characters")
    void validateTitle_MustReturnViolations_WhenTitleSizeIsBiggerThan100Characters() {
      String titleWith101Characters = "title xptotitle xptotitle xptotitle xptotitle xptotitle xptotitle xptotitle xptotitle xptotitle xpto.";
      VideoRequest videoRequest = new VideoRequest(titleWith101Characters, VALID_DESCRIPTION, VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(titleWith101Characters).hasSize(101);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("title must be maximum 100 characters long");
    }

  }

  @Nested
  @DisplayName("Tests for description validation")
  class DescriptionValidation {

    @ParameterizedTest
    @ValueSource(strings = {"Description XPTO", "D"})
    @DisplayName("Validate description must not return violations when description is valid")
    void validateDescription_MustNotReturnViolations_WhenDescriptionIsValid(String description) {
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, description, VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate description must return violations when description is null")
    void validateDescription_MustReturnViolations_WhenDescriptionIsNull() {
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, null, VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("description must not be null or blank");
    }

    @Test
    @DisplayName("Validate description must return violations when description is empty")
    void validateDescription_MustReturnViolations_WhenDescriptionIsEmpty() {
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, "", VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("description must not be null or blank");
    }

    @Test
    @DisplayName("Validate description must return violations when description is blank")
    void validateDescription_MustReturnViolations_WhenDescriptionIsBlank() {
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, "   ", VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("description must not be null or blank");
    }

    @Test
    @DisplayName("Validate description must return violations when description size is bigger than 255 characters")
    void validateDescription_MustReturnViolations_WhenDescriptionSizeIsBiggerThan100Characters() {
      String descriptionWith256Characters = "description xptodescription xptodescription xptodescription xpto" +
          "description xptodescription xptodescription xptodescription xpto" +
          "description xptodescription xptodescription xptodescription xpto" +
          "description xptodescription xptodescription xptodescription xpto";
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, descriptionWith256Characters, VALID_URL, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(descriptionWith256Characters).hasSize(256);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("description must be maximum 255 characters long");
    }

  }

  @Nested
  @DisplayName("Tests for url validation")
  class UrlValidation {

    @Test
    @DisplayName("Validate url must not return violations when url is valid")
    void validateUrl_MustNotReturnViolations_WhenUrlIsValid() {
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, VALID_DESCRIPTION, "http://www.xptovideos.com", 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate url must return violations when url is null")
    void validateUrl_MustReturnViolations_WhenUrlIsNull() {
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, VALID_DESCRIPTION, null, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("url must not be null or blank");
    }

    @Test
    @DisplayName("Validate url must return violations when url is empty")
    void validateUrl_MustReturnViolations_WhenUrlIsEmpty() {
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, VALID_DESCRIPTION, "", 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("url must not be null or blank");
    }

    @Test
    @DisplayName("Validate url must return violations when url is blank")
    void validateUrl_MustReturnViolations_WhenUrlIsBlank() {
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, VALID_DESCRIPTION, "   ", 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);
      List<String> actualListOfMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
      Assertions.assertThat(actualListOfMessages).contains("url must not be null or blank");
    }

    @Test
    @DisplayName("Validate url must return violations when url size is bigger than 255 url")
    void validateUrl_MustReturnViolations_WhenUrlSizeIsBiggerThan100Characters() {
      String urlWith256Characters = "http://www.xptovideos.com/loremlorem" +
          "loremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremlorem" +
          "loremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremloremlorem";
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, VALID_DESCRIPTION, urlWith256Characters, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(urlWith256Characters).hasSize(256);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("url must be maximum 255 characters long");
    }

    @ParameterizedTest
    @ValueSource(strings = {"http", "://www.xptovideos.com"})
    @DisplayName("Validate url must return violations when url isn't a well-formed url")
    void validateUrl_MustReturnViolations_WhenUrlIsntAWellFormedUrl(String invalidUrl) {
      VideoRequest videoRequest = new VideoRequest(VALID_TITLE, VALID_DESCRIPTION, invalidUrl, 1);

      Set<ConstraintViolation<VideoRequest>> actualViolations = validator.validate(videoRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("must be a well-formed url");
    }

  }

}