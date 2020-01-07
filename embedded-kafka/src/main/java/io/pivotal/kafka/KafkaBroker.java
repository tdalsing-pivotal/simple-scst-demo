package io.pivotal.kafka;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
    public static EmbeddedKafkaBroker broker() {
        log.info("broker");
        EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(1, false);
        embeddedKafkaBroker.brokerProperties(convert(System.getProperties()));
        return embeddedKafkaBroker;
    }

    private static Map<String, String> convert(Properties properties) {
        Map<String, String> map = new HashMap<>();

        for (Object key : properties.keySet()) {
            String skey = key.toString();
            map.put(skey, properties.getProperty(skey));
        }

        return map;
    }
}
