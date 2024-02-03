package com.pbl.loadtestweb.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.pbl.loadtestweb.*"})
@EntityScan(basePackages = {"com.pbl.loadtestweb.*"})
@EnableJpaRepositories(basePackages = {"com.pbl.loadtestweb.*"})
public class LoadTestWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(LoadTestWebApplication.class, args);
  }
}
