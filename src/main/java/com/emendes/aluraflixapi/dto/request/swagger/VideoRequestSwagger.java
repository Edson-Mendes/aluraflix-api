package com.emendes.aluraflixapi.dto.request.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

public interface VideoRequestSwagger {

  @Schema(description = "Título do vídeo", required = true, example = "Lorem ipsum dolor sit amet")
  String getTitle();
  @Schema(description = "Descrição do vídeo", required = true, example = "Vídeo legalzão sobre xpto")
  String getDescription();
  @Schema(description = "URL do vídeo", required = true, example = "http://www.xpto.com/d8273te7629g7")
  String getUrl();
  @Schema(description = "Categoria do vídeo", required = false, defaultValue = "1", example = "1")
  Integer getCategoryId();

}
