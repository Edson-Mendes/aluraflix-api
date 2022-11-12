package com.emendes.aluraflixapi.model.entity;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "tb_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @Column(nullable = false)
  private boolean enabled;
  private LocalDateTime deletedAt;
  @ManyToMany(fetch = FetchType.EAGER)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinTable(
      name = "tb_user_roles",
      joinColumns = @JoinColumn(name = "user_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
  )
  private Set<Role> roles = new HashSet<>();

  public void addRole(Role role) {
    if (roles == null) {
      roles = new HashSet<>();
    }
    roles.add(role);
  }
}
