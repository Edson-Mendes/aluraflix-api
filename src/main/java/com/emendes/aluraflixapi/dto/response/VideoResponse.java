package com.emendes.aluraflixapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoResponse {

  private Long id;
  private String title;
  private String description;
  private String url;
  private Integer categoryId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    VideoResponse that = (VideoResponse) o;

    if (!Objects.equals(id, that.id)) return false;
    if (!Objects.equals(title, that.title)) return false;
    if (!Objects.equals(description, that.description)) return false;
    return Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    return result;
  }

}
