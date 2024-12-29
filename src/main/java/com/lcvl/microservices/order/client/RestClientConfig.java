package com.lcvl.microservices.order.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

	@Value("${inventory.url}")
	private String inventoryServiceUrl;
	private final ObservationRegistry observationRegistry;
	
	@Bean
	public InventoryClient inventoryClient() {
		RestClient customClient = RestClient.builder()
				.baseUrl(inventoryServiceUrl)
				.requestFactory(getClientRequestFactory())
				.observationRegistry(observationRegistry) //trace kafka
				.build();
		var restClientAdapter = RestClientAdapter.create(customClient);
		var httpServiceProxyfactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
		return httpServiceProxyfactory.createClient(InventoryClient.class);
	}
	
    private ClientHttpRequestFactory getClientRequestFactory() {
        ClientHttpRequestFactorySettings clientHttpRequestFactorySettings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(3))
                .withReadTimeout(Duration.ofSeconds(3));
        return ClientHttpRequestFactories.get(clientHttpRequestFactorySettings);
    }
}
