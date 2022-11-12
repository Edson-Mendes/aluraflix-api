package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.mapper.UserMapper;
import com.emendes.aluraflixapi.model.entity.User;
import com.emendes.aluraflixapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder encoder;


  @Override
  public UserResponse signUp(UserRequest userRequest) {
    User user = userMapper.fromUserRequest(userRequest);
    user.setPassword(encoder.encode(userRequest.getPassword()));
    user.setCreatedAt(LocalDateTime.now());
//    TODO: adicionar roles!
    return userMapper.toUserResponse(userRepository.save(user));
  }

}
