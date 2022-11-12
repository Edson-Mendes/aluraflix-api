package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;

public interface AuthenticationService {

  UserResponse signUp(UserRequest userRequest);

}
