package com.nce.backend.common.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InstantFutureValidator.class)
public @interface InstantInFuture {
    String message() default "Specified time must be in future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
