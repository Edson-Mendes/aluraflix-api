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

//  TODO: Pesquisar como devolver uma mensagem mais amigável para o cliente.
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ExceptionDetails> handleDataIntegrityViolation(
      DataIntegrityViolationException exception, HttpServletRequest request) {
    ExceptionDetails details = ExceptionDetails.builder()
        .status(HttpStatus.CONFLICT.value())
        .title("Data conflict")
        .details(exception.getMessage())
        .timestamp(LocalDateTime.now())
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(details);
  }

}
