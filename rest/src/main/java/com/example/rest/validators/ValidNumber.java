package com.example.rest.validators;

import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CalculatorNumberValidator.class)
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNumber {
    String message() default "Invalid Number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}