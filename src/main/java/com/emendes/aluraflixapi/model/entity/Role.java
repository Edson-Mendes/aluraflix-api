package com.emendes.aluraflixapi.model.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "tb_role")
public class Role implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(nullable = false, unique = true)
  private String name;

  @Override
  public String getAuthority() {
    return this.name;
  }
}
