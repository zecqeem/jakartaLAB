package org.example.jakartalabs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.jakartalabs.model.Apartment;

public class ValidApartmentValidator implements ConstraintValidator<ValidApartment, Apartment> {
    @Override
    public boolean isValid(Apartment apt, ConstraintValidatorContext context) {
        if (apt == null) return true;
        // Якщо 3+ кімнати — ціна повинна бути >= 10000 (преміум-клас)
        if (apt.getRooms() >= 3 && apt.getPrice() < 10000) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Квартира з " + apt.getRooms() + " кімнатами повинна коштувати щонайменше 10000 грн"
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
