package com.emendes.aluraflixapi.unit.mapper;

import com.emendes.aluraflixapi.config.ModelMapperConfig;
import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.mapper.UserMapper;
import com.emendes.aluraflixapi.mapper.UserMapperImpl;
import com.emendes.aluraflixapi.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for UserMapperImpl")
class UserMapperImplTest {

  private UserMapper userMapper;

  @BeforeEach
  void setUp() {
    userMapper = new UserMapperImpl(new ModelMapperConfig().getModelMapper());
  }

  @Test
  @DisplayName("toUserResponse must return UserResponse when map successfully")
  void toUserResponse_MustReturnUserResponse_WhenMapSuccessfully() {
    User userToBeMapped = User.builder().id(100L).name("Lorem Ipsum").email("lorem@email.com")
        .createdAt(LocalDateTime.parse("2022-11-05T10:00:00"))
        .enabled(true).build();

    UserResponse actualUserResponse = userMapper.toUserResponse(userToBeMapped);
    UserResponse expectedUserResponse = new UserResponse(100L, "Lorem Ipsum", "lorem@email.com");

    Assertions.assertThat(actualUserResponse)
        .isNotNull()
        .isEqualTo(expectedUserResponse);
  }

  @Test
  @DisplayName("fromUserRequest must return User when map successfully")
  void fromUserRequest_MustReturnUser_WhenMapSuccessfully() {
    UserRequest userRequestToBeMapped = new UserRequest("Lorem Ipsum", "lorem@email.com", "123456");

    User actualUser = userMapper.fromUserRequest(userRequestToBeMapped);
    User expectedUser = User.builder().name("Lorem Ipsum").email("lorem@email.com").password("123456").build();

    Assertions.assertThat(actualUser).isNotNull();
    Assertions.assertThat(actualUser.getId()).isNull();
    Assertions.assertThat(actualUser.getCreatedAt()).isNull();
    Assertions.assertThat(actualUser.getDeletedAt()).isNull();
    Assertions.assertThat(actualUser.isEnabled()).isTrue();
    Assertions.assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
    Assertions.assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
    Assertions.assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());

  }

}
