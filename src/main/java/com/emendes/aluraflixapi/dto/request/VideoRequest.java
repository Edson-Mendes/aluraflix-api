package com.emendes.aluraflixapi.dto.request;

import com.emendes.aluraflixapi.dto.request.groups.CreateInfo;
import com.emendes.aluraflixapi.dto.request.groups.UpdateInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public class VideoRequest {

  @NotBlank(message = "title must not be null or blank", groups = {CreateInfo.class})
  @Size(max = 100, message = "title must be maximum {max} characters long", groups = {CreateInfo.class})
  private String title;

  @Size(max = 255, message = "description must be maximum {max} characters long", groups = {CreateInfo.class})
  @NotBlank(message = "description must not be null or blank", groups = {CreateInfo.class})
  private String description;

  @Size(max = 255, message = "url must be maximum {max} characters long", groups = {CreateInfo.class})
  @NotBlank(message = "url must not be null or blank", groups = {CreateInfo.class})
  @URL(message = "must be a well-formed url", groups = {CreateInfo.class})
  private String url;

  @NotNull(message = "categoryId must not be null", groups = {UpdateInfo.class})
  private Integer categoryId;

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

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
