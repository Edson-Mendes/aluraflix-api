package com.emendes.aluraflixapi.controller.swagger;

import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@OpenAPIDefinition(tags = @Tag(name = "Authentication"))
public interface AuthenticationControllerSwagger {

  @Operation(summary = "Criar usuário", tags = {"Authentication"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "409", description = "Email já está em uso", content = @Content)
  })
  ResponseEntity<UserResponse> signUp(UserRequest userRequest, UriComponentsBuilder uriBuilder);

}
