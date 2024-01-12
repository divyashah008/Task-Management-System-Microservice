package com.task.controller;

import com.task.entity.Task;
import com.task.exception.EmptyInputException;
import com.task.exception.UserNotFoundException;
import com.task.entity.request.TaskRequest;
import com.task.response.ErrorResponse;
import com.task.response.SuccessResponse;
import com.task.response.TaskResponse;
import com.task.service.TaskService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  private final TaskService taskService;

  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

//  @Tag(name = "get", description = "It shows all the Task data")
//  @Operation(summary = "Get all Task", description = "It shows all the Task data")
//  @GetMapping
//  public ResponseEntity<?> getAllTask(@RequestParam(defaultValue = "0") int page,
//      @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "id") String sortBy) {
//    try {
//      Page<TaskResponse> taskPage = taskService.getAllTask(page, size,sortBy);
//      return ResponseEntity.ok(taskPage.getContent());
//    } catch (Exception e) {
//      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
//    }
//  }

  @Tag(name = "get", description = "It shows all the Task data")
  @Operation(summary = "Get all Task", description = "It shows all the Task data")
  @GetMapping
  public List<Task> getAllTask1() {
     return taskService.getAllTasksWithoutPagination();
  }


  //Get All Task By Id
//  @Tag(name = "get", description = "It shows Task data by id")
//  @Operation(summary = "Get Task by id", description = "It shows Task data by id")
//  @GetMapping("/{id}")
//  public ResponseEntity<?>  getTaskById(@PathVariable Long id){
//    try {
//    TaskResponse task =taskService.getTaskById(id);
//    return ResponseEntity.ok(task);
//  } catch (UserNotFoundException e) {
//    return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(),404));
//  } catch (Exception e) {
//    return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
//  }
//  }

  @Tag(name = "get", description = "It shows all the Task data")
  @Operation(summary = "Get all Task", description = "It shows all the Task data")
  @GetMapping("/{id}")
  @CircuitBreaker(name = "taskUserBreaker",fallbackMethod = "getTaskUserFallback")
  public ResponseEntity<Optional<Task>> getTaskById(@PathVariable Long id) {
  //  try {
      Optional<Task> task = taskService.getTaskById1(id);
      return ResponseEntity.status(200).body(task);
//    } catch (UserNotFoundException e) {
//      return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(), 404));
//    } catch (Exception e) {
//      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), 404));
//    }
  }

  //Creating fallback method for circuit breaker
  public  ResponseEntity<Task> getTaskUserFallback(Long id,Exception ex){
    System.out.println("Fallback is executed=="+ex.getMessage());
    Task task=Task.builder().name("dummy")
    .description("This is fallback method")
    .build();
    return new ResponseEntity<>(task, HttpStatus.OK);
  }




  @Operation(summary = "Add Task data", description = "Add the Task")
  @PostMapping(value="/save")
  public ResponseEntity<?> createTask(@RequestBody Task task) {
    try {
      TaskResponse createdTask = taskService.createTask(task);
      return ResponseEntity.status(201).body(createdTask);
    } catch (EmptyInputException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),400));
    }
  }

  //Delete Task
  @Operation(summary = "Delete Task data", description = "Delete Task by id")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deleteTask(@PathVariable Long id) {
    try {
      taskService.deleteTask(id);
      return ResponseEntity.ok(new SuccessResponse("Task deleted successfully",200));
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    }
  }



  //Update Task
  @Operation(summary = "Update Task data", description = "Update an existing task. The response is updated user object with Id,Name.")
  @PutMapping(value = "/{id}")
  public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
    try {
      TaskResponse updatedTask = taskService.updateTask(id, taskRequest)
                                            .orElseThrow(() -> new UserNotFoundException("Task not found with ID: " + id));
      return ResponseEntity.ok(updatedTask);
    } catch (EmptyInputException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    } catch (UserNotFoundException e ) {
      return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e ) {
      return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage(),400));
    }
  }

}
