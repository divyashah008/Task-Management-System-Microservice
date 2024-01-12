package com.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskServiceException extends RuntimeException{

  public TaskServiceException(String message, Throwable cause) {

    super(message,cause);
  }

}
