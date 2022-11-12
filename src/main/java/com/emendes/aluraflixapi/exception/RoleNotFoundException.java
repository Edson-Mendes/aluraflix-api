package com.emendes.aluraflixapi.exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends NotFoundException{

  public RoleNotFoundException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }

}
