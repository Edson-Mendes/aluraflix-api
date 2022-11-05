package com.emendes.aluraflixapi.exception;

import org.springframework.http.HttpStatus;

public class VideoNotFoundException extends NotFoundException {

  public VideoNotFoundException(String message) {
    this(message, HttpStatus.NOT_FOUND);
  }

  public VideoNotFoundException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }

}
