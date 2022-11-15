package com.emendes.aluraflixapi.config.security;

import com.emendes.aluraflixapi.dto.response.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {

    ExceptionDetails details = ExceptionDetails.builder()
        .status(HttpStatus.UNAUTHORIZED.value())
        .title("Unauthorized")
        .details(exception.getMessage())
        .timestamp(LocalDateTime.now())
        .build();

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter()
        .write(details.json());
    response.getWriter().flush();

  }

}
