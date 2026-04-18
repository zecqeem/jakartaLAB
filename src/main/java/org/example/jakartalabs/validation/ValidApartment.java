package org.example.jakartalabs.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidApartmentValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidApartment {
    String message() default "Квартира з 3+ кімнатами повинна коштувати щонайменше 10000 грн";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
