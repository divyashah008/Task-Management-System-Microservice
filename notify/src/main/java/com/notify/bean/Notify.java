package com.notify.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Notify {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String recipient;
  private String message;

  public Notify(String recipient, String message) {
    this.recipient = recipient;
    this.message = message;
  }

  @Override
  public String toString() {
    return "Notify{" +
        "id=" + id +
        ", recipient='" + recipient + '\'' +
        ", message='" + message + '\'' +
        '}';
  }


}
