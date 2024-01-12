package com.prorigo.controller;

import com.prorigo.exception.EmptyInputException;
import com.prorigo.exception.UserNotFoundException;
import com.prorigo.model.request.NotificationRequest;
import com.prorigo.model.request.UserRequest;
import com.prorigo.model.response.ErrorResponse;
import com.prorigo.model.response.SuccessResponse;
import com.prorigo.model.response.UserResponse;
import com.prorigo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Tag(name = "get", description = "It shows all the User data")
  @Operation(summary = "Get all Users", description = "It shows all the user data")
  @GetMapping
  public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "id") String sortBy) {
    try {
      Page<UserResponse> userPage = userService.getAllUsers(page, size,sortBy);
      return ResponseEntity.ok(userPage.getContent());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    }
  }


  @Tag(name = "get", description = "It shows the User data by id")
  @Operation(summary = "Get User By Id", description = "It shows particular user data with the help of user id")
  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getUserById(@PathVariable Long id) {
    try {
      UserResponse user = userService.getUserById(id);
      return ResponseEntity.ok(user);
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    }
  }



//  @GetMapping("/{id}")
//  public List<Task> getUserByIdByFeign(@PathVariable Long id) {
//    return userService.getTasksForUser(id);
//  }


  @Operation(summary = "Add User data", description = "Add the User")
  @PostMapping(value="/save")
  public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
    try {
      UserResponse createdUser = userService.createUser(userRequest);
      return ResponseEntity.status(201).body(createdUser);
    } catch (EmptyInputException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),400));
    }
  }


  @Operation(summary = "Update User data", description = "Update an existing user. The response is updated user object with Id,Name.")
  @PutMapping(value = "/{id}")
  public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
    try {
      UserResponse updatedUser = userService.updateUser(id, userRequest)
                                            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
      return ResponseEntity.ok(updatedUser);
    } catch (EmptyInputException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    } catch (UserNotFoundException e ) {
      return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e ) {
      return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage(),400));
    }
  }


  @Operation(summary = "Delete User data", description = "Delete User by id")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    try {
      userService.deleteUser(id);
      return ResponseEntity.ok(new SuccessResponse("User deleted successfully",200));
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    }
  }

  @PostMapping("/{userId}/notifications")
  public ResponseEntity<?> sendNotification(@PathVariable Long userId, @RequestBody NotificationRequest notificationRequest) {
    try {
      userService.sendNotification(userId,notificationRequest.getMessage());
      return ResponseEntity.ok(new SuccessResponse("Send successfully",200));
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    }
  }


}
