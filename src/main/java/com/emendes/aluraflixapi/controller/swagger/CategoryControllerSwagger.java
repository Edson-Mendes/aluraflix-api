package com.emendes.aluraflixapi.controller.swagger;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@OpenAPIDefinition(tags = @Tag(name = "Category"), security = {@SecurityRequirement(name = "Basic auth")})
public interface CategoryControllerSwagger {

  @Operation(summary = "Buscar todas as categorias", tags = {"Category"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categorias encontradas com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content)
  })
  Page<CategoryResponse> findAll(@ParameterObject Pageable pageable);

  @Operation(summary = "Buscar categoria por id", tags = {"Category"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
  })
  ResponseEntity<CategoryResponse> findById(int id);

  @Operation(summary = "Buscar vídeos por categoria", tags = {"Category"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Encontrou videos dessa categoria com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
  })
  ResponseEntity<Page<VideoResponse>> findVideosByCategory(int id, @ParameterObject Pageable pageable);

  @Operation(summary = "Salvar uma categoria", tags = {"Category"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Categoria salva com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content),
      @ApiResponse(responseCode = "409", description = "Já existe categoria com esse título", content = @Content),
  })
  ResponseEntity<CategoryResponse> create(CategoryRequest categoryRequest, UriComponentsBuilder uriBuilder);

  @Operation(summary = "Atualizar categoria por id", tags = {"Category"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição"),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
      @ApiResponse(responseCode = "409", description = "Já existe categoria com esse título", content = @Content),
  })
  ResponseEntity<CategoryResponse> update(int id, CategoryRequest categoryRequest);

  @Operation(summary = "Deletar categoria por id", tags = {"Category"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Categoria deletado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição"),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
  })
  ResponseEntity<Void> delete(int id);

}
