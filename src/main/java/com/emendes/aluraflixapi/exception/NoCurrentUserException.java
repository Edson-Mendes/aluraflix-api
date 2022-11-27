package com.emendes.aluraflixapi.exception;

public class NoCurrentUserException extends RuntimeException {

  public NoCurrentUserException(String message) {
    super(message);
  }

}
