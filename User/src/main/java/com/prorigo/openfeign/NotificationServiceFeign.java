package com.prorigo.openfeign;

import com.prorigo.model.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${app.feign.config.name}" ,url = "${app.feign.config.url}")
public interface NotificationServiceFeign {

  @PostMapping("/send")
  void sendNotification(@RequestBody NotificationRequest notificationRequest);

}
