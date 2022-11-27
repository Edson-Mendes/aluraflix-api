package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.ChangePasswordRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.exception.PasswordException;
import com.emendes.aluraflixapi.exception.UserNotFoundException;
import com.emendes.aluraflixapi.mapper.UserMapper;
import com.emendes.aluraflixapi.model.entity.User;
import com.emendes.aluraflixapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final CurrentUser currentUser;

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
  public void changePassword(ChangePasswordRequest changePasswordRequest) {
    User current = currentUser.getCurrentUser();
    if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), current.getPassword())) {
      throw new PasswordException("Wrong old password");
    }
    if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())){
      throw new PasswordException("Passwords do not match");
    }
    current.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    userRepository.save(current);
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
