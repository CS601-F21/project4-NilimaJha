package validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation class for transferee field on TransferTicket class.
 * @author nilimajha
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransfereeEmailValidation.class)
public @interface TransfereeEmail {
    String message() default ("User with Given Email Id does not exist. Enter Valid User.");
    Class<?>[] groups() default {};
    public abstract Class<? extends Payload>[] payload() default {};
}
