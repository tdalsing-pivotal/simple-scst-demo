package io.pivotal.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableConfigurationProperties(RetryProperties.class)
@Slf4j
public class RetryConfig {

    @Bean
    public static RetryTemplate retryTemplate(RetryProperties properties) {
        log.info("retryTemplate: properties={}", properties);

        NeverRetryPolicy neverRetryPolicy = new NeverRetryPolicy();
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(properties.getMaxAttempts());

        ExceptionClassifier exceptionClassifier = new ExceptionClassifier(properties.getMaxAttempts(), properties.getRetryableSqlErrors(), properties.getRetryableSqlStates());
        ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy = new ExceptionClassifierRetryPolicy();
        exceptionClassifierRetryPolicy.setExceptionClassifier(exceptionClassifier);

        RetryTemplate retryTemplate = new RetryTemplate();

        retryTemplate.setRetryPolicy(exceptionClassifierRetryPolicy);

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(properties.getInitialInterval());
        exponentialBackOffPolicy.setMaxInterval(properties.getMaxInterval());
        exponentialBackOffPolicy.setMultiplier(properties.getMultiplier());

        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        return retryTemplate;
    }

}
