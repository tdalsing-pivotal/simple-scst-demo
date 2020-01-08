package io.pivotal.kafka;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaBroker {

    EmbeddedKafkaBroker broker;

    public KafkaBroker(EmbeddedKafkaBroker broker) {
        this.broker = broker;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaBroker.class, args);
    }

    @Bean
    public static EmbeddedKafkaBroker broker() throws Exception {
        log.info("broker");

        InetAddress localHost = InetAddress.getLocalHost();
        String hostName = localHost.getHostAddress();
        String listeners = String.format("PLAINTEXT://%s:9092", hostName);
        log.info("broker: listeners={}", listeners);

        EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(1, false);
        Map<String, String> brokerProperties = new HashMap<>();
        brokerProperties.put("listeners", listeners);
        embeddedKafkaBroker.brokerProperties(brokerProperties);
        return embeddedKafkaBroker;
    }

}
