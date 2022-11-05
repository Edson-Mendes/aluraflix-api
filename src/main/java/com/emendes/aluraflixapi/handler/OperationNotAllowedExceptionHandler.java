package com.emendes.aluraflixapi.handler;

import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import com.emendes.aluraflixapi.exception.OperationNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class OperationNotAllowedExceptionHandler {

  @ExceptionHandler(OperationNotAllowedException.class)
  public ResponseEntity<ExceptionDetails> handle(OperationNotAllowedException exception, HttpServletRequest request) {
    ExceptionDetails details = ExceptionDetails.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .title("Operation not allowed")
        .details(exception.getMessage())
        .timestamp(LocalDateTime.now())
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
  }

}
