package com.emendes.aluraflixapi.handler;

import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class DataIntegrityViolationExceptionHandler {

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ExceptionDetails> handleDataIntegrityViolation(
      DataIntegrityViolationException exception, HttpServletRequest request) {
    ExceptionDetails details = ExceptionDetails.builder()
        .status(HttpStatus.CONFLICT.value())
        .title("Data conflict")
        .details(messageResolver(exception.getMessage()))
        .timestamp(LocalDateTime.now())
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(details);
  }

  private String messageResolver(String message) {
    if (message == null)
      return "";
    if (message.contains("f_email_unique"))
      return "email is already in use";
    if (message.contains("f_title_unique"))
      return "the given title already exists";
    return message;
  }

}
