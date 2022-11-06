package com.emendes.aluraflixapi.dto.request.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

public interface CategoryRequestSwagger {

  @Schema(description = "Título da categoria", required = true, example = "Comédia")
  String getTitle();
  @Schema(description = "Color em hexadecimal sem '#'", required = true, example = "e7f416")
  String getColor();

}
