package com.emendes.aluraflixapi.mapper;

import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapperImpl implements UserMapper {

  private final ModelMapper mapper;

  @Override
  public User fromUserRequest(UserRequest userRequest) {
    return mapper.map(userRequest, User.class);
  }

  @Override
  public UserResponse toUserResponse(User user) {
    return mapper.map(user, UserResponse.class);
  }

}
