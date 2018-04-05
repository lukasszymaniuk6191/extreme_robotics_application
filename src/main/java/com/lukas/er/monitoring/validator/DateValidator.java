package com.lukas.er.monitoring.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class DateValidator implements ConstraintValidator<ValidDate, String> {
    public void initialize(ValidDate constraint) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(value.trim());
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }

    }
}