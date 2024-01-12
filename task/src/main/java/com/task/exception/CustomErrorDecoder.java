package com.task.exception;


import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CustomErrorDecoder implements ErrorDecoder {


  @Override
  public Exception decode(String s, Response response) {
    if (response.status() == HttpStatus.NOT_FOUND.value()) {
      // Handle 404 Not Found
      return new UserNotFoundException("Resource not found");
    }
    else if (response.status() == HttpStatus.BAD_REQUEST.value()) {
      // Handle 404 Not Found
      return new EmptyInputException("Input fields are empty ,Please look into it");
    }
    return new Exception("Unknown error");
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  public static class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
      super(message);
    }
  }
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public static class EmptyInputException extends RuntimeException{
    public EmptyInputException(String message) {
      super(message);
    }
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public static class GenericException extends RuntimeException{
    public GenericException(String message) {
      super(message);
    }
  }
}
