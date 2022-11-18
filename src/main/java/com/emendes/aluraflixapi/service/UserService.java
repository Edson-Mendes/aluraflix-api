package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  Page<UserResponse> fetchAll(Pageable pageable);

  UserResponse findById(Long id);

}
