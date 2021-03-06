package validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation class for eventDate field on event class.
 * @author nilimajha
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventDateValidation.class)
public @interface EventDate {
    String message() default ("Date should be upcoming date.");
    Class<?>[] groups() default {};
    public abstract Class<? extends Payload>[] payload() default {};
}
