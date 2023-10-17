package io.pivotal.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ValidationSupport {

    Validator validator;

    private static final ObjectMapper mapper = new ObjectMapper();

    public ValidationSupport(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object obj) throws Exception {
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);

        if (!violations.isEmpty()) {
            Map[] array = violations.stream().map(v -> {
                Map<String, Object> mv = new TreeMap<>();

                mv.put("message", v.getMessage());
                mv.put("rootBean", mapper.convertValue(v.getRootBean(), Map.class));
                mv.put("rootBeanClass", v.getRootBeanClass().getName());
                mv.put("invalidValue", v.getInvalidValue());
                mv.put("property", v.getPropertyPath().toString());
                mv.put("annotation", v.getConstraintDescriptor().getAnnotation().annotationType().getName());

                return mv;
            }).toArray(Map[]::new);
            String s = mapper.writeValueAsString(array);
            log.info("validate: s={}", s);
            throw new ValidationException(s);
        }
    }
}
