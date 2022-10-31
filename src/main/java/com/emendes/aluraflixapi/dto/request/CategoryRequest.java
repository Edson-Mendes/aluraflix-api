package com.emendes.aluraflixapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public class CategoryRequest {

  @NotBlank(message = "title must not be null or blank")
  @Size(max = 50, message = "title must be maximum {max} characters long")
  private String title;
//  TODO: Criar uma anotation para validar a cor no formato #00000000 a #ffffffff.
  @NotBlank(message = "color must not be null or blank")
  private String color;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CategoryRequest that = (CategoryRequest) o;

    if (!Objects.equals(title, that.title)) return false;
    return Objects.equals(color, that.color);
  }

  @Override
  public int hashCode() {
    int result = title != null ? title.hashCode() : 0;
    result = 31 * result + (color != null ? color.hashCode() : 0);
    return result;
  }

}