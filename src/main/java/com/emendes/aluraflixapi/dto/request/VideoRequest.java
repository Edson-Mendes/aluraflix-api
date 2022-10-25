package com.emendes.aluraflixapi.dto.request;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
public class VideoRequest {

  @NotBlank(message = "title must not be null or blank")
  @Size(max = 100, message = "title must be between {min} and {max} characters long")
  private String title;

  @Size(max = 255, message = "description must be between {min} and {max} characters long")
  @NotBlank(message = "description must not be null or blank")
  private String description;

  @Size(max = 255, message = "url must be between {min} and {max} characters long")
  @NotBlank(message = "url must not be null or blank")
  @URL(message = "must be a well-formed url")
  private String url;

}
