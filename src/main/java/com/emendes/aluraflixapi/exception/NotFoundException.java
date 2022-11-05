package com.emendes.aluraflixapi.exception;

import org.springframework.http.HttpStatus;

public abstract class NotFoundException extends RuntimeException {

  private final HttpStatus httpStatus;

  protected NotFoundException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}
