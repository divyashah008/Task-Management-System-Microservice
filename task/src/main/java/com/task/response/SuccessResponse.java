package com.task.response;

import lombok.Data;

@Data
public class SuccessResponse {

  private String message;
  private int successCode;

  public SuccessResponse(String message,int successCode) {
    this.message = message;
    this.successCode = successCode;
  }

}
