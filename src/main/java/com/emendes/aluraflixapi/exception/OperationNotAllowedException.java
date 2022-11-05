package com.emendes.aluraflixapi.exception;

public class OperationNotAllowedException extends RuntimeException {
  public OperationNotAllowedException(String message) {
    super(message);
  }

}
