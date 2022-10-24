package com.emendes.aluraflixapi.handler;

import com.emendes.aluraflixapi.dto.response.ValidationExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MethodArgumentNotValidHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationExceptionDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request){
    List<FieldError> fieldErrors = exception.getFieldErrors();

    String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining("; "));
    String messages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));

    ValidationExceptionDetails details = ValidationExceptionDetails.builder()
        .title("Bad Request")
        .status(HttpStatus.BAD_REQUEST.value())
        .timestamp(LocalDateTime.now())
        .details("Invalid field(s)")
        .fields(fields)
        .messages(messages)
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.badRequest().body(details);
  }

}
