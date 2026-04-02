package com.devfolio.backend;

import com.devfolio.backend.config.AppSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppSecurityProperties.class)
public class DevfolioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevfolioBackendApplication.class, args);
    }
}
