package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

  @Override
  public UserResponse signUp(UserRequest userRequest) {
    return new UserResponse(1000L, userRequest.getName(), userRequest.getEmail());
  }

}
