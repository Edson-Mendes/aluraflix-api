package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.exception.RoleNotFoundException;
import com.emendes.aluraflixapi.model.entity.Role;
import com.emendes.aluraflixapi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  @Override
  public Role getDefaultRole() {
    return roleRepository.findByName("ROLE_USER")
        .orElseThrow(() -> new RoleNotFoundException("Role not found", HttpStatus.INTERNAL_SERVER_ERROR));
  }

}
