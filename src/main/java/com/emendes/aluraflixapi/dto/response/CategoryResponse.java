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
public class CategoryResponse {

  private Integer id;
  private String title;
  private String color;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CategoryResponse that = (CategoryResponse) o;

    if (!Objects.equals(id, that.id)) return false;
    if (!Objects.equals(title, that.title)) return false;
    return Objects.equals(color, that.color);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (color != null ? color.hashCode() : 0);
    return result;
  }

}
