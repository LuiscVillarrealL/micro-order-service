package com.lcvl.microservices.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI inventoryServiceAPI() {
		
		return new OpenAPI()
				.info(new Info().title("Inventory Service API")
						.description("Rest API for inventory service")
						.version("v0.0.1")
						.license(new License().name("Apache 2.0")))
				.externalDocs(new ExternalDocumentation()
						.description("Refer to wiki: TODO")
						.url("http....docs"));
	}

}
