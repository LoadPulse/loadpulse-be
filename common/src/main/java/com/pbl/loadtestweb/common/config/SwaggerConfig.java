package com.pbl.loadtestweb.common.config;

import com.pbl.loadtestweb.common.constant.CommonConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    final Set<String> typeValue = new HashSet<>();
    typeValue.add(MediaType.APPLICATION_JSON_VALUE);
    typeValue.add(MediaType.APPLICATION_XML_VALUE);

    return new Docket(DocumentationType.SWAGGER_2)
        .produces(typeValue)
        .consumes(typeValue)
        .select()
        .apis(RequestHandlerSelectors.basePackage(CommonConstant.BASE_PACKAGE_ENDPOINT))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .securityContexts(Collections.singletonList(securityContext()))
        .securitySchemes(Collections.singletonList(getApiKey()));
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "API Documentation - Load Testing Project",
        "Api for Load Testing Project",
        "V1",
        "NA terms of service url",
        "Contact",
        "A license given",
        "NA");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder().securityReferences(defaultAuth()).build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return List.of(new SecurityReference("Bearer Token", authorizationScopes));
  }

  private ApiKey getApiKey() {
    return new ApiKey("Bearer Token", "Authorization", "Header");
  }
}
