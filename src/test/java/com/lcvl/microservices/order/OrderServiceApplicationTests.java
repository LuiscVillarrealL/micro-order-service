package com.lcvl.microservices.order;


import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import com.lcvl.microservices.commons.order.event.OrderPlacedEvent;
import com.lcvl.microservices.order.stubs.InventoryClientStub;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = OrderServiceApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0) //0 = random
class OrderServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");
    @LocalServerPort
    private Integer port;
    
    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mySQLContainer.start();
    }
    

    @Test
    void shouldSubmitOrder() {
        String submitOrderJson = """
               {
    "skuCode": "iphone_15",
    "quantity": 1,
    "price" : 1000,

    "userDetails": {
    "email": "test@kafka.com",
    "firstName": "test",
    "lastName": "kafka"
  }


}
                """;

        InventoryClientStub.stubInventoryCall("iphone_15", 1);

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();

        
        assertThat(responseBodyString, Matchers.is("Order Placed Successfully"));
    }
}

