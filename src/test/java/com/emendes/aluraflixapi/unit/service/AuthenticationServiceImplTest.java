package com.emendes.aluraflixapi.unit.service;

import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.exception.RoleNotFoundException;
import com.emendes.aluraflixapi.mapper.UserMapper;
import com.emendes.aluraflixapi.model.entity.Role;
import com.emendes.aluraflixapi.model.entity.User;
import com.emendes.aluraflixapi.repository.UserRepository;
import com.emendes.aluraflixapi.service.AuthenticationServiceImpl;
import com.emendes.aluraflixapi.service.RoleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for AuthenticationService")
class AuthenticationServiceImplTest {

  @InjectMocks
  private AuthenticationServiceImpl authenticationService;
  @Mock
  private UserRepository userRepositoryMock;
  @Mock
  private UserMapper userMapperMock;
  @Mock
  private RoleService roleServiceMock;
  @Mock
  private PasswordEncoder encoderMock;

  @Test
  @DisplayName("signUp must return UserResponse when SignUp successfully")
  void signUp_MustReturnUserResponse_WhenSignUpSuccessfully() {
    UserRequest userRequest = new UserRequest("Lorem Ipsum", "lorem@email.com", "123456");
    User user = User.builder().name("Lorem Ipsum").email("lorem@email.com").password("123456")
        .createdAt(LocalDateTime.parse("2022-11-05T10:00:00")).enabled(true).build();
    Role role = new Role(1, "ROLE_USER");

    BDDMockito.when(userMapperMock.fromUserRequest(userRequest)).thenReturn(user);
    BDDMockito.when(roleServiceMock.getDefaultRole()).thenReturn(role);
    BDDMockito.when(encoderMock.encode("123456")).thenReturn("654321");
    user.setId(100000L);
    BDDMockito.when(userRepositoryMock.save(ArgumentMatchers.any(User.class))).thenReturn(user);
    UserResponse userResponse = new UserResponse(100000L, "Lorem Ipsum", "lorem@email.com");
    BDDMockito.when(userMapperMock.toUserResponse(ArgumentMatchers.any(User.class))).thenReturn(userResponse);

    UserResponse actualUserResponse = authenticationService.signUp(userRequest);
    UserResponse expectedUserResponse = new UserResponse(100000L, "Lorem Ipsum", "lorem@email.com");

    Assertions.assertThat(actualUserResponse).isNotNull().isEqualTo(expectedUserResponse);
  }

  @Test
  @DisplayName("signUp must throws RoleNotFoundException when not found default role")
  void signUp_MustThrowsRoleNotFoundException_WhenNotFoundDefaultRole() {
    UserRequest userRequest = new UserRequest("Lorem Ipsum", "lorem@email.com", "123456");
    User user = User.builder().name("Lorem Ipsum").email("lorem@email.com").password("123456")
        .createdAt(LocalDateTime.parse("2022-11-05T10:00:00")).enabled(true).build();

    BDDMockito.when(userMapperMock.fromUserRequest(userRequest)).thenReturn(user);
    BDDMockito.willThrow(new RoleNotFoundException("Role not found", HttpStatus.INTERNAL_SERVER_ERROR))
        .given(roleServiceMock).getDefaultRole();

    Assertions.assertThatExceptionOfType(RoleNotFoundException.class)
        .isThrownBy(() -> authenticationService.signUp(userRequest))
        .withMessage("Role not found");
  }

  @Test
  @DisplayName("signUp must throws DataIntegrityViolationException when email already exists on DB")
  void signUp_MustThrowsDataIntegrityViolationException_WhenEmailAlreadyExistsOnDB() {
    UserRequest userRequest = new UserRequest("Lorem Ipsum", "lorem@email.com", "123456");
    User user = User.builder().name("Lorem Ipsum").email("lorem@email.com").password("123456")
        .createdAt(LocalDateTime.parse("2022-11-05T10:00:00")).enabled(true).build();
    Role role = new Role(1, "ROLE_USER");

    BDDMockito.when(userMapperMock.fromUserRequest(userRequest)).thenReturn(user);
    BDDMockito.when(roleServiceMock.getDefaultRole()).thenReturn(role);
    BDDMockito.when(encoderMock.encode("123456")).thenReturn("654321");
    BDDMockito.willThrow(new DataIntegrityViolationException("f_email_unique constraint"))
        .given(userRepositoryMock).save(ArgumentMatchers.any(User.class));

    Assertions.assertThatExceptionOfType(DataIntegrityViolationException.class)
        .isThrownBy(() -> authenticationService.signUp(userRequest))
        .withMessage("f_email_unique constraint");
  }

}