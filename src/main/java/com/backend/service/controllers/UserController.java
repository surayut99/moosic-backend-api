package com.backend.service.controllers;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.exceptions.users.NotFoundUserException;
import com.backend.service.models.entities.UserModel;
import com.backend.service.models.requests.users.BasedUserCreationRequest;
import com.backend.service.services.entity.AuthTokenService;
import com.backend.service.services.entity.UserService;
import com.backend.service.views.users.UserPublicView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private AuthTokenService authTokenService;

  @PutMapping("/update/{id}")
  public ResponseEntity<?> update(@ModelAttribute BasedUserCreationRequest data, @PathVariable("id") UUID id) throws MoosicException {
    UserModel user = userService.updateUser(data, id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/display-name/{displayName}")
  public ResponseEntity<?> getByUsername(@PathVariable String displayName) {
    List<UserModel> users = userService.findByDisplayName(displayName);
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/")
  public ResponseEntity<?> getAll() {
    List<UserModel> users = userService.getAll();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundUserException {
    UserModel user = userService.getById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/profile/{id}")
  public ResponseEntity<?> getProfileById(@PathVariable UUID id) throws NotFoundUserException {
    UserPublicView user = userService.getProfileById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> delete(@PathVariable UUID id) {
    userService.delete(id);
    return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
  }
}
