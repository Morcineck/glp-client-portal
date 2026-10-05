package com.glp.client_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class ClientPortalApplication {

    public static void main(String[] args) {
        SpringApplication application =
                new SpringApplication(ClientPortalApplication.class);

        application.setDefaultProperties(
                Map.of("spring.flyway.enabled", "false")
        );

        application.run(args);
    }
}
