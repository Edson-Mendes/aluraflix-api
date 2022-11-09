package com.emendes.aluraflixapi.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "tb_category")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(nullable = false, unique = true)
  private String title;
  @Column(nullable = false)
  private String color;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  private LocalDateTime deletedAt;
  private boolean enabled;

  public Category(Integer id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Category category = (Category) o;

    if (enabled != category.enabled) return false;
    if (!Objects.equals(id, category.id)) return false;
    if (!Objects.equals(title, category.title)) return false;
    if (!Objects.equals(color, category.color)) return false;
    if (!Objects.equals(createdAt, category.createdAt)) return false;
    return Objects.equals(deletedAt, category.deletedAt);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (color != null ? color.hashCode() : 0);
    result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
    result = 31 * result + (deletedAt != null ? deletedAt.hashCode() : 0);
    result = 31 * result + (enabled ? 1 : 0);
    return result;
  }

}
