package com.task.service;

import com.task.exception.EmptyInputException;
import com.task.exception.TaskServiceException;
import com.task.exception.UserNotFoundException;
import com.task.entity.Task;
import com.task.entity.request.TaskRequest;
import com.task.openfeign.UserServiceFeign;
import com.task.repository.TaskRepo;
import com.task.response.TaskResponse;
import com.task.entity.User;
import feign.FeignException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private final TaskRepo taskRepo;
  private final UserServiceFeign userServiceFeign;


  @Autowired
  public TaskService(TaskRepo taskRepo,UserServiceFeign userServiceFeign) {
    this.taskRepo = taskRepo;
    this.userServiceFeign=userServiceFeign;
  }

  public Page<TaskResponse> getAllTask(int page, int size,String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Task> taskPage = taskRepo.findAll(pageable);
    return taskPage.map(task -> new TaskResponse(task.getId(), task.getName(),task.getDescription()));
  }

  public List<Task> getAllTasksWithoutPagination() {
    try {
      List<Task> tasks = taskRepo.findAll();
      tasks.forEach(task -> {
      //  try {
          User user = userServiceFeign.getUserById(task.getUser_id().getId());
          task.setUser_id(user);
       // } catch (UserNotFoundException e) {
       //     task.setUser_id(new User("Default User", "default@example.com"));
      //  }
      });
      return tasks;
    } catch (FeignException e) {
      throw new TaskServiceException("Error communicating with UserService", e);
    }
  }

  public TaskResponse getTaskById(Long id) {
    Task task = taskRepo.findById(id)
                              .orElseThrow(() -> new UserNotFoundException("Task not found with ID: " + id));
    return new TaskResponse(task.getId(), task.getName(),task.getDescription());
  }


  public Optional<Task> getTaskById1(Long id) {
    Optional<Task> taskOptional = Optional.ofNullable(taskRepo.findById(id).orElseThrow(
        () -> new UserNotFoundException("Task not found with ID: " + id)));

    taskOptional.ifPresent(task -> {
       User user = userServiceFeign.getUserById(task.getUser_id().getId());
      task.setUser_id(user);
    });
    return taskOptional;
  }

  public TaskResponse createTask(Task task) {
    if (task.getName() == null || task.getName().trim().isEmpty()) {
      throw new EmptyInputException("Task name cannot be empty");
    }

    User user = userServiceFeign.getUserById(task.getUser_id().getId());
    if (user == null) {
      // Handle the case when the user is not found (optional)
      throw new UserNotFoundException("User not found for taskId: " + task.getUser_id());
    }
    task.setUser_id(user);

    Task tasks = new Task();
    tasks.setName(task.getName());
    tasks.setDescription(task.getDescription());
    Task createdTask = taskRepo.save(tasks);

    return new TaskResponse(createdTask.getId(), createdTask.getName(),createdTask.getDescription());
  }

  public void deleteTask(Long id) {
    if (taskRepo.existsById(id)) {
      taskRepo.deleteById(id);
    } else {
      throw new UserNotFoundException("Task not found with ID: " + id);
    }
  }

  public Optional<TaskResponse> updateTask(Long id, TaskRequest taskRequest) {
    Optional<Task> existingTask = taskRepo.findById(id);
    System.out.println(existingTask);
    return existingTask.map(task -> {
      if (taskRequest.getName() != null && !taskRequest.getName().trim().isEmpty()) {
        // Update task fields
        task.setName(taskRequest.getName());
        task.setDescription(taskRequest.getDescription());
        // Update other fields as needed
        taskRepo.save(task);
        return new TaskResponse(task.getId(), task.getName(),task.getDescription());
      } else {
        throw new EmptyInputException("Task name cannot be empty");
      }
    });
  }
}
