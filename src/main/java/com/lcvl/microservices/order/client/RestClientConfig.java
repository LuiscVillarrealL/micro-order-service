package com.lcvl.microservices.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

	@Value("${inventory.url}")
	private String inventoryServiceUrl;
	
	@Bean
	public InventoryClient inventoryClient() {
		RestClient customClient = RestClient.builder()
				.baseUrl(inventoryServiceUrl)
				.build();
		var restClientAdapter = RestClientAdapter.create(customClient);
		var httpServiceProxyfactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
		return httpServiceProxyfactory.createClient(InventoryClient.class);
	}
}
