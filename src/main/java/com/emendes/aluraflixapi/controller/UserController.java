package com.emendes.aluraflixapi.controller;

import com.emendes.aluraflixapi.controller.swagger.UserControllerSwagger;
import com.emendes.aluraflixapi.dto.request.ChangePasswordRequest;
import com.emendes.aluraflixapi.dto.response.UserResponse;
import com.emendes.aluraflixapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController implements UserControllerSwagger {

  private final UserService userService;

  @GetMapping
  @Override
  public ResponseEntity<Page<UserResponse>> fetchAll(@PageableDefault(size = 5) Pageable pageable) {
    return ResponseEntity.ok(userService.fetchAll(pageable));
  }

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<UserResponse> findById(@PathVariable(name = "id") long id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @PutMapping("/password")
  @Override
  public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
    userService.changePassword(changePasswordRequest);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
