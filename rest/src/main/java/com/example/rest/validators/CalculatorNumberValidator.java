package com.example.rest.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class CalculatorNumberValidator implements ConstraintValidator<ValidNumber, String> {

    @Override
    public void initialize(ValidNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String number, ConstraintValidatorContext context) {
        try {
            new BigDecimal(number);
            return true;
        } catch (NumberFormatException e){
            System.out.println(e);
            return false;
        }
    }
}
