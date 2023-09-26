package com.example.healthcareservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.val;

import java.time.LocalDate;

public class DateBirthValidator implements ConstraintValidator<DateBirthValidation, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        val dateForEighteenYears = LocalDate.now().minusYears(18);
        val olderThanEighteen = birthDate.isBefore(dateForEighteenYears) ||
                dateForEighteenYears.isEqual(birthDate);
        return birthDate.isBefore(LocalDate.now()) && olderThanEighteen;
    }
}
