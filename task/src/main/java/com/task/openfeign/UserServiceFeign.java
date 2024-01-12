package com.task.openfeign;

import com.task.config.CustomFeignConfiguration;
import com.task.entity.User;
import com.task.exception.UserNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "${app.feign.config.name}" ,url = "${app.feign.config.url}",configuration = CustomFeignConfiguration.class)
public interface UserServiceFeign {


  @GetMapping("/{userId}")
  User getUserById(@PathVariable Long userId) ;

  @PostMapping("/save")
  User createUser(User values);

  @PutMapping(value = "/{userId}")
  User updateUser(@PathVariable Long userId,User values) throws UserNotFoundException;

  @DeleteMapping(value = "/{userId}")
  void deleteUser(@PathVariable Long userId);

}
