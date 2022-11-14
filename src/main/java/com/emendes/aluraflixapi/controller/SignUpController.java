package com.emendes.aluraflixapi.controller;

import com.emendes.aluraflixapi.controller.swagger.SignUpControllerSwagger;
import com.emendes.aluraflixapi.dto.request.UserRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class SignUpController implements SignUpControllerSwagger {

  private final SignUpService signUpService;

  @Override
  @PostMapping("/signup")
  public ResponseEntity<UserResponse> signUp(
      @RequestBody @Valid UserRequest userRequest, UriComponentsBuilder uriBuilder) {
    UserResponse userResponse = signUpService.signUp(userRequest);
    URI uri = uriBuilder.path("/users/{id}").build(userResponse.getId());
    return ResponseEntity.created(uri).body(userResponse);
  }

}
