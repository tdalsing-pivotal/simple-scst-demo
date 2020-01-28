package io.pivotal.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@ConfigurationProperties("retry")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RetryProperties {

    int maxAttempts = 3;
    long initialInterval = 500L;
    long maxInterval = 10000L;
    double multiplier = 3.0;
    Set<Integer> retryableSqlErrors = new HashSet<>();
    Set<String> retryableSqlStates = new HashSet<>();
}
