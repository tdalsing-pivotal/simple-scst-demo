package io.pivotal.common;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@Import(RetryConfig.class)
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RetrySupport {

    RetryTemplate retryTemplate;
    MessageChannel errorChannel;

    public RetrySupport(RetryTemplate retryTemplate, MessageChannel errorChannel) {
        this.retryTemplate = retryTemplate;
        this.errorChannel = errorChannel;
    }

    public <T, R> R execute(RetryFunction<T, R> function, T message) {
        try {
            log.info("execute: message={}", message);
            return retryTemplate.execute(context -> function.apply(message));
        } catch (Exception e) {
            log.error("execute: message={}, e={}", message, e.toString());

            if (message instanceof Message) {
                Message<?> msg = (Message<?>) message;
                errorChannel.send(MessageBuilder.withPayload(msg.getPayload()).build());
            } else {
                errorChannel.send(MessageBuilder.withPayload(message).build());
            }

            return null;
        }
    }
}
