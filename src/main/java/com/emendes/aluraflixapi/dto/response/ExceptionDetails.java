package com.emendes.aluraflixapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class ExceptionDetails {

  private String title;
  private int status;
  private String details;
  private LocalDateTime timestamp;
  private String path;

  public String json() {
    String jsonTemplate = """
        {
          "title" : "%s",
          "status" : %d,
          "details" : "%s",
          "timestamp" : "%s",
          "path" : "%s"
        }
        """;

    return String.format(jsonTemplate, title, status, details, timestamp, path);
  }
}
