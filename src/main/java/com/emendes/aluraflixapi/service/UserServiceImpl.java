package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.exception.UserNotFoundException;
import com.emendes.aluraflixapi.mapper.UserMapper;
import com.emendes.aluraflixapi.model.entity.User;
import com.emendes.aluraflixapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    User user = findUserById(id);
    return userMapper.toUserResponse(user);
  }

  @Override
  public void delete(Long id) {
    User user = findUserById(id);

    user.setEnabled(false);
    user.setDeletedAt(LocalDateTime.now());

    userRepository.save(user);
  }

  private User findUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found for id: "+ id));
  }

}
