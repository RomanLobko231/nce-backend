package com.nce.backend.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

public class InstantFutureValidator implements ConstraintValidator<InstantInFuture, Instant> {
    @Override
    public boolean isValid(Instant instant, ConstraintValidatorContext constraintValidatorContext) {
        return instant != null && instant.isAfter(Instant.now());
    }
}
