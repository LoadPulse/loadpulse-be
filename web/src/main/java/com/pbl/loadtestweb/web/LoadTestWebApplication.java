package com.pbl.loadtestweb.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(basePackages = {"com.pbl.loadtestweb.*"})
@EnableWebMvc
public class LoadTestWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(LoadTestWebApplication.class, args);
  }
}
