package com.prorigo.model.response;

import lombok.Data;

@Data
public class UserResponse {
  private Long id;
  private String userName;
  private String email;


  public UserResponse(Long id, String userName,String email) {
    this.id = id;
    this.userName = userName;
    this.email = email;
  }

}
