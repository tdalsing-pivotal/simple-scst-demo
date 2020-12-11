package io.pivotal.producer;

import io.pivotal.common.MyObject;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootApplication
@RestController
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProducerApp {

    StreamBridge bridge;

    public ProducerApp(StreamBridge bridge) {
        this.bridge = bridge;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }

    @PostMapping("/publish")
    @ResponseStatus(CREATED)
    public void publish(@RequestBody MyObject myObject) {
        log.info("publish: myObject={}", myObject);
        bridge.send("source-out-0", myObject);
    }
}
