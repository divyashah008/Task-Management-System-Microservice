package com.task.response;

import com.task.entity.Task;
import java.util.Optional;
import lombok.Data;

@Data
public class TaskResponse {
  private Long id;
  private String name;
  private String description;



  public TaskResponse(Long id, String name,String description) {
    this.id = id;
    this.name = name;
    this.description=description;
  }


}
