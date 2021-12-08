package validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserBirthDateValidation.class)
public @interface UserBirthDate {

    String message() default("Date should be from past.");
    Class<?>[] groups() default {};
    public abstract Class<? extends Payload>[] payload() default {};
}
