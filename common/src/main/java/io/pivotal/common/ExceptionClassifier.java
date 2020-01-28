package io.pivotal.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.classify.Classifier;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import javax.validation.ValidationException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ExceptionClassifier implements Classifier<Throwable, RetryPolicy> {

    RetryPolicy neverRetryPolicy = new NeverRetryPolicy();
    RetryPolicy simpleRetryPolicy;
    Map<Class<?>, RetryPolicy> policyMap = new HashMap<>();
    Set<Integer> retryableSqlErrors;
    Set<String> retryableSqlStates;

    public ExceptionClassifier(int maxAttempts, Set<Integer> retryableSqlErrors, Set<String> retryableSqlStates) {
        this.retryableSqlErrors = retryableSqlErrors;
        this.retryableSqlStates = retryableSqlStates;
        this.simpleRetryPolicy = new SimpleRetryPolicy(maxAttempts);

        policyMap.put(NullPointerException.class, neverRetryPolicy);
        policyMap.put(ValidationException.class, neverRetryPolicy);
        policyMap.put(JsonProcessingException.class, neverRetryPolicy);

        policyMap.put(IOException.class, simpleRetryPolicy);
        policyMap.put(SocketTimeoutException.class, simpleRetryPolicy);
    }

    @Override
    public RetryPolicy classify(Throwable classifiable) {
        log.info("classify: classifiable={}", classifiable);

        RetryPolicy policy = policyMap.get(classifiable.getClass());

        if (policy == null && classifiable instanceof SQLException) {
            SQLException sqlException = (SQLException) classifiable;

            if (retryableSqlErrors.contains(sqlException.getErrorCode())) {
                policy = simpleRetryPolicy;
            }

            if (policy == null && retryableSqlStates.contains(sqlException.getSQLState())) {
                policy = simpleRetryPolicy;
            }
        }

        if (policy == null && classifiable.getCause() != null) {
            policy = classify(classifiable.getCause());
        }

        if (policy == null) {
            policy = neverRetryPolicy;
        }

        log.info("classify: classifiable={}, policy={}", classifiable, policy.getClass().getName());
        return policy;
    }
}
