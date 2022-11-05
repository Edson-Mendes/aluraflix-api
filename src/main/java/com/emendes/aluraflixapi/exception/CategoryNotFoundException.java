package com.emendes.aluraflixapi.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends NotFoundException {

  public CategoryNotFoundException(String message) {
    this(message, HttpStatus.NOT_FOUND);
  }

  public CategoryNotFoundException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }

}
