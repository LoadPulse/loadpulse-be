package com.pbl.loadpulse.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.pbl.loadpulse.*"})
@EntityScan(basePackages = {"com.pbl.loadpulse.*"})
@EnableJpaRepositories(basePackages = {"com.pbl.loadpulse.*"})
public class LoadPulseApplication {

  public static void main(String[] args) {
    SpringApplication.run(LoadPulseApplication.class, args);
  }
}
