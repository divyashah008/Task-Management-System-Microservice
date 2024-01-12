package com.notify.controller;

import com.notify.bean.Notify;
import com.notify.service.NotificationService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class NotificationCtl {

  private final NotificationService notificationService;

  public NotificationCtl(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping
  public List<Notify> getAllNotify(){
   return notificationService.getAllNotify();
  }

  @PostMapping("/save")
  public Notify saveNotify(@RequestBody Notify notify){
    return notificationService.createNotify(notify);
  }

  @DeleteMapping("/{id}")
  public void deleteNotify(@PathVariable Long id){
     notificationService.deleteNotify(id);
  }

  @PostMapping("/send")
  public void sendNotification(@RequestBody Notify notify) {
    notificationService.sendNotification(notify.getRecipient(), notify.getMessage());
  }

}
