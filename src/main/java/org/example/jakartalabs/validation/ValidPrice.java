package org.example.jakartalabs.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPriceValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {
    String message() default "Ціна має бути кратна 100";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
