package com.emendes.aluraflixapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {

  private String fields;
  private String messages;

}
