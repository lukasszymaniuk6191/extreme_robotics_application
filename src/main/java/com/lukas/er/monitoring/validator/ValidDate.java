package com.lukas.er.monitoring.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface ValidDate {

    String message() default "Wrong date format";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}


