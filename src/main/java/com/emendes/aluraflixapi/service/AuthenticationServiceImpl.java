package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.mapper.UserMapper;
import com.emendes.aluraflixapi.model.entity.Role;
import com.emendes.aluraflixapi.model.entity.User;
import com.emendes.aluraflixapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder encoder;
  private final RoleService roleService;


  @Override
  public UserResponse signUp(UserRequest userRequest) {
    User user = userMapper.fromUserRequest(userRequest);
    Role role = roleService.getDefaultRole();

    user.addRole(role);
    user.setPassword(encoder.encode(userRequest.getPassword()));

    return userMapper.toUserResponse(userRepository.save(user));
  }

}
