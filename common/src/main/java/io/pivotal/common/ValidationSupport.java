package io.pivotal.common;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ValidationSupport {

    Validator validator;

    public ValidationSupport(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object obj) {
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);

        if (!violations.isEmpty()) {
            throw new ValidationException(violations.toString());
        }
    }
}
