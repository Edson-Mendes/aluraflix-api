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
public class ChangePasswordRequest {

  @NotBlank(message = "oldPassword must not be null or blank")
  private String oldPassword;
  @NotBlank(message = "newPassword must not be null or blank")
  @Size(min = 6, max = 25, message = "newPassword must be between {min} and {max} characters long")
  private String newPassword;
  @NotBlank(message = "confirmPassword must not be null or blank")
  private String confirmPassword;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ChangePasswordRequest that = (ChangePasswordRequest) o;

    if (!Objects.equals(oldPassword, that.oldPassword)) return false;
    if (!Objects.equals(newPassword, that.newPassword)) return false;
    return Objects.equals(confirmPassword, that.confirmPassword);
  }

  @Override
  public int hashCode() {
    int result = oldPassword != null ? oldPassword.hashCode() : 0;
    result = 31 * result + (newPassword != null ? newPassword.hashCode() : 0);
    result = 31 * result + (confirmPassword != null ? confirmPassword.hashCode() : 0);
    return result;
  }

}
