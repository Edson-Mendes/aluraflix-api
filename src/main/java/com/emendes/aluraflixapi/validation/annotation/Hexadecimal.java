package com.emendes.aluraflixapi.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element must be a valid hexadecimal.<br><br>
 * {@code null} elements are considered valid.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HexaDecimalValidator.class)
public @interface Hexadecimal {

  String message() default "must be a valid hexadecimal";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

}
