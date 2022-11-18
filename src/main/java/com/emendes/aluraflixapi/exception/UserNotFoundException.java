package com.emendes.aluraflixapi.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }

}
