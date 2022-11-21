package com.emendes.aluraflixapi.unit.service;

import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.exception.UserNotFoundException;
import com.emendes.aluraflixapi.mapper.UserMapper;
import com.emendes.aluraflixapi.model.entity.User;
import com.emendes.aluraflixapi.repository.UserRepository;
import com.emendes.aluraflixapi.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for UserService")
class UserServiceImplTest {

  @InjectMocks
  private UserServiceImpl userService;
  @Mock
  private UserRepository userRepositoryMock;
  @Mock
  private UserMapper userMapperMock;
  private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 5);

  @Nested
  @DisplayName("Tests for fetchAll method")
  class FetchAllMethod {

    @Test
    @DisplayName("fetchAll must return Page<UserResponse> when fetch successfully")
    void fetchAll_MustReturnPageUserResponse_WhenFetchSuccessfully() {
      BDDMockito.when(userRepositoryMock.findAll(DEFAULT_PAGEABLE))
          .thenReturn(pageUser());
      BDDMockito.when(userMapperMock.toUserResponse(ArgumentMatchers.any(User.class)))
          .thenReturn(userResponse());

      Page<UserResponse> actualPageUserResponse = userService.fetchAll(DEFAULT_PAGEABLE);
      UserResponse expectedUserResponse = new UserResponse(1000L, "Lorem Ipsum", "lorem@email.com");

      Assertions.assertThat(actualPageUserResponse)
          .isNotNull().hasSize(1).contains(expectedUserResponse);
    }

  }

  @Nested
  @DisplayName("Tests for findById method")
  class FindByIdMethod {

    @Test
    @DisplayName("findById must return UserResponse when found successfully")
    void findById_MustReturnUserResponse_WhenFoundSuccessfully() {
      BDDMockito.when(userRepositoryMock.findById(1000L))
          .thenReturn(optionalUser());
      BDDMockito.when(userMapperMock.toUserResponse(ArgumentMatchers.any(User.class)))
          .thenReturn(userResponse());

      UserResponse actualUserResponse = userService.findById(1000L);

      Assertions.assertThat(actualUserResponse).isNotNull();
      Assertions.assertThat(actualUserResponse.getId()).isEqualTo(1000L);
      Assertions.assertThat(actualUserResponse.getName()).isEqualTo("Lorem Ipsum");
      Assertions.assertThat(actualUserResponse.getEmail()).isEqualTo("lorem@email.com");
    }

    @Test
    @DisplayName("findById must throws UserNotFoundException when not found user")
    void findById_MustThrowsUserNotFoundException_WhenNotFoundUser() {
      BDDMockito.when(userRepositoryMock.findById(9999L))
          .thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(UserNotFoundException.class)
          .isThrownBy(() -> userService.findById(9999L))
          .withMessage("User not found for id: "+ 9999L);
    }

  }

  private User user() {
    LocalDateTime createdAt = LocalDateTime.parse("2022-11-08T10:00:00");
    return User.builder()
        .id(1000L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("12345678")
        .enabled(true)
        .createdAt(createdAt)
        .build();
  }

  private UserResponse userResponse() {
    return new UserResponse(1000L, "Lorem Ipsum", "lorem@email.com");
  }

  private Page<User> pageUser() {
    return new PageImpl<>(List.of(user()), DEFAULT_PAGEABLE, 1);
  }

  private Optional<User> optionalUser() {
    return Optional.of(user());
  }

}