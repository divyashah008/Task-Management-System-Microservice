package com.prorigo.advice;

import com.prorigo.exception.EmptyInputException;
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

}
