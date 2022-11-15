package com.emendes.aluraflixapi.controller.swagger;

import com.emendes.aluraflixapi.dto.request.VideoRequest;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public interface VideoControllerSwagger {

  @Operation(summary = "Buscar todos os vídeos, opcional buscar por título", tags = {"Video"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vídeos encontrados com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content)
  })
  ResponseEntity<Page<VideoResponse>> findAll(String title, @ParameterObject Pageable pageable);

  @Operation(summary = "Buscar vídeo por id", tags = {"Video"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vídeo encontrado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content),
      @ApiResponse(responseCode = "404", description = "Vídeo não encontrado", content = @Content),
  })
  ResponseEntity<VideoResponse> findById(long id);


  @Operation(summary = "Buscar os 5 últimos vídeos adicionados, não necessita autenticação", tags = {"Video"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vídeos encontrados com sucesso")
  })
  ResponseEntity<List<VideoResponse>> fetchFreeSample();

  @Operation(summary = "Salvar um vídeo", tags = {"Video"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Vídeo salvo com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content)
  })
  ResponseEntity<VideoResponse> create(VideoRequest videoRequest, UriComponentsBuilder uriBuilder);

  @Operation(summary = "Atualizar vídeo por id", tags = {"Video"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vídeo atualizado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição"),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content),
      @ApiResponse(responseCode = "404", description = "Vídeo não encontrado")
  })
  ResponseEntity<VideoResponse> update(long id, VideoRequest videoRequest);

  @Operation(summary = "Deletar vídeo por id", tags = {"Video"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Vídeo deletado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição"),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content),
      @ApiResponse(responseCode = "404", description = "Vídeo não encontrado"),
  })
  ResponseEntity<Void> delete(long id);

}
