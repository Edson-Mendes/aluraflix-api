package com.emendes.aluraflixapi.mapper;


import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.model.entity.User;

public interface UserMapper {

  User fromUserRequest(UserRequest userRequest);

  UserResponse toUserResponse(User user);

}
