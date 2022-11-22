package com.emendes.aluraflixapi.controller.swagger;

import com.emendes.aluraflixapi.dto.response.UserResponse;
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

@OpenAPIDefinition(tags = @Tag(name = "User"), security = {@SecurityRequirement(name = "Basic auth")})
public interface UserControllerSwagger {

  @Operation(summary = "Buscar todos os usuários", tags = {"User"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuários encontrado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "403", description = "Usuário não tem permissão de acesso", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content)
  })
  ResponseEntity<Page<UserResponse>> fetchAll(@ParameterObject Pageable pageable);

  @Operation(summary = "Buscar usuário por id", tags = {"User"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized, falha na autenticação", content = @Content),
      @ApiResponse(responseCode = "403", description = "Usuário não tem permissão de acesso", content = @Content),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
  })
  ResponseEntity<UserResponse> findById(long id);
}
