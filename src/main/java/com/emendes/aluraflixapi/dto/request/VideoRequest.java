package com.emendes.aluraflixapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public class VideoRequest {

  @NotBlank(message = "title must not be null or blank")
  @Size(max = 100, message = "title must be maximum {max} characters long")
  private String title;

  @Size(max = 255, message = "description must be maximum {max} characters long")
  @NotBlank(message = "description must not be null or blank")
  private String description;

  @Size(max = 255, message = "url must be maximum {max} characters long")
  @NotBlank(message = "url must not be null or blank")
  @URL(message = "must be a well-formed url")
  private String url;

  private Integer categoryId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    VideoRequest that = (VideoRequest) o;

    if (!Objects.equals(title, that.title)) return false;
    if (!Objects.equals(description, that.description)) return false;
    return Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    int result = title != null ? title.hashCode() : 0;
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    return result;
  }
}
