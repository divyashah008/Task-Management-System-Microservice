package com.task.config;

import com.task.exception.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignConfiguration {

  @Bean
  public ErrorDecoder errorDecoder() {
    return new CustomErrorDecoder();
  }


}
