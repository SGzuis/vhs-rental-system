package com.locadora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableJpaAuditing
public class LocadoraApplication {
    public static void main(String[] args) {
        SpringApplication.run(LocadoraApplication.class, args);
    }
}
