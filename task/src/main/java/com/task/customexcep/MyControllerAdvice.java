package com.task.customexcep;

import com.task.exception.EmptyInputException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice {

  @ExceptionHandler(EmptyInputException.class)
  public ResponseEntity<String> handleEmptyInput(EmptyInputException emptyInputException){
    return new ResponseEntity<>("Input fields are empty ,Please look into it",
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> handleEmptyInput(NoSuchElementException userNotFoundException){
    return new ResponseEntity<String>("No value is present in DB, Please change your request",
        HttpStatus.NOT_FOUND);
  }

}
