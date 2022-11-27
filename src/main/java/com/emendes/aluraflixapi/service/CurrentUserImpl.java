package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.exception.NoCurrentUserException;
import com.emendes.aluraflixapi.model.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserImpl implements CurrentUser {

  @Override
  public User getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(principal instanceof User user) {
      return user;
    }
    throw new NoCurrentUserException("No current user");
  }

}
