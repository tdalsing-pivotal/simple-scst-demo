package io.pivotal.consumer;

import io.pivotal.common.MyObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
@Slf4j
public class ConsumerApp {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }

    @Bean
    public Consumer<MyObject> consume() {
        log.info("consume");
        return object -> log.info("consume: object = {}", object);
    }
}
