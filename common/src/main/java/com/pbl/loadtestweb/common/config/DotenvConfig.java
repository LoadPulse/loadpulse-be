package com.pbl.loadtestweb.common.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

  @Bean
  public Dotenv dotenv() {
    return Dotenv.configure().filename("environment.env").load();
  }
}
