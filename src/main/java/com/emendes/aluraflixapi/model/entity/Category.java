package com.emendes.aluraflixapi.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

}
