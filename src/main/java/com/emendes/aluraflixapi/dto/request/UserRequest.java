package com.emendes.aluraflixapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public class UserRequest {

  @NotBlank(message = "name must not be null or blank")
  @Size(max = 100, message = "name must be {max} characters long")
  private String name;
  @NotBlank(message = "email must not be null or blank")
  @Email(message = "email must be a valid email")
  @Size(max = 255, message = "email must be {max} characters long")
  private String email;
  @NotBlank(message = "password must not be null or blank")
  @Size(min = 6, message = "password must be at least 6 characters long")
  private String password;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserRequest that = (UserRequest) o;

    if (!Objects.equals(name, that.name)) return false;
    if (!Objects.equals(email, that.email)) return false;
    return Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }

}
