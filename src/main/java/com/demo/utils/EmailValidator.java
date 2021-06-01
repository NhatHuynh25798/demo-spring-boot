package com.demo.utils;


import com.demo.annotation.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;

public class EmailValidator implements Serializable, ConstraintValidator<Email, String> {
    @Override
    public void initialize(Email constraintAnnotation) {

    }

    @Override
    public boolean isValid(String emailAddress, ConstraintValidatorContext constraintValidatorContext) {
        if (emailAddress == null || emailAddress.trim().length() == 0) {
            return false;
        }
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(emailAddress);
    }
}
