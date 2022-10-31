package com.emendes.aluraflixapi.handler;

import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import com.emendes.aluraflixapi.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ResourceNotFoundExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ExceptionDetails> handleResourceNotFound(NotFoundException exception, HttpServletRequest request) {
    ExceptionDetails details = ExceptionDetails.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .title("Resource not found")
        .details(exception.getMessage())
        .timestamp(LocalDateTime.now())
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(details);
  }

}
