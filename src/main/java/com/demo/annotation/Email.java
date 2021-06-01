package com.demo.annotation;

import com.demo.utils.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface Email {
    String message() default "email is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
