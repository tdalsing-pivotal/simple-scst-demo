package io.pivotal.consumer;

import io.pivotal.common.MyObject;
import io.pivotal.common.RetrySupport;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Import;

import javax.validation.ValidationException;
import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;

@SpringBootApplication
@EnableBinding(Sink.class)
@Import(RetrySupport.class)
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ConsumerApp {

    RetrySupport retrySupport;

    public ConsumerApp(RetrySupport retrySupport) {
        this.retrySupport = retrySupport;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void receive(MyObject myObject) {
        log.info("receive: myObject={}", myObject);
        retrySupport.execute(arg -> check(myObject), myObject);
    }

    private MyObject check(MyObject obj) throws Exception {
        log.info("check: obj={}", obj);

        if (obj.getCount() < 0) {
            throw new IOException("Count is less than zero: "+obj);
        }

        if (obj.getAmount() < 0.0) {
            throw new ValidationException("Amount is less than zero: "+obj);
        }

        return obj;
    }
}
