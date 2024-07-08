package com.pbl.loadpulse.common.config;

import com.pbl.loadpulse.common.constant.CommonConstant;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().components(new Components()).info(apiInfo());
  }

  private Info apiInfo() {
    return new Info()
        .title("Springdoc API")
        .description("Springdoc API Documentation")
        .version("1.0.0");
  }

  @Bean
  public OpenAPI customize() {
    Components components = new Components();

    SecurityScheme scheme =
        new SecurityScheme()
            .name(CommonConstant.BEARER_AUTH)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");

    components.addSecuritySchemes(CommonConstant.BEARER_AUTH, scheme);

    return new OpenAPI()
        .components(components)
        .addSecurityItem(new SecurityRequirement().addList(CommonConstant.BEARER_AUTH));
  }
}
