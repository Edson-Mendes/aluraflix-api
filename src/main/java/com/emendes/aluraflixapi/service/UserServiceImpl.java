package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.exception.UserNotFoundException;
import com.emendes.aluraflixapi.mapper.UserMapper;
import com.emendes.aluraflixapi.model.entity.User;
import com.emendes.aluraflixapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public Page<UserResponse> fetchAll(Pageable pageable) {
    return userRepository.findAll(pageable).map(userMapper::toUserResponse);
  }

  @Override
  public UserResponse findById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found for id: "+ id));

    return userMapper.toUserResponse(user);
  }

}
