package io.pivotal.producer;

import io.pivotal.common.MyObject;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootApplication
@RestController
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProducerApp {

    EmitterProcessor<MyObject> processor;

    public ProducerApp(EmitterProcessor<MyObject> processor) {
        this.processor = processor;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }

    @Bean
    public static EmitterProcessor<MyObject> processor() {
        log.info("processor");
        return EmitterProcessor.create();
    }

    @Bean
    public static Supplier<Flux<MyObject>> source(EmitterProcessor<MyObject> processor) {
        log.info("source");
        return () -> Flux.from(processor);
    }

    @PostMapping("/publish")
    @ResponseStatus(CREATED)
    public void publish(@RequestBody MyObject myObject) {
        log.info("publish: myObject={}", myObject);
        processor.onNext(myObject);
    }
}
