package com.emendes.aluraflixapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ExceptionDetails {

  private String title;
  private int status;
  private String details;
  private LocalDateTime timestamp;
  private String path;

}
