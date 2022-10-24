package com.emendes.aluraflixapi.exception;

public class VideoNotFoundException extends RuntimeException{

  public VideoNotFoundException(String message) {
    super(message);
  }

}
