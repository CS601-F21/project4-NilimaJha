package validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation class for transferTicketIdList field on event class.
 * @author nilimajha
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransferTicketIdListValidation.class)
public @interface TransferTicketIdList {
//    String message() default "No Ticket Selected.";
    String message() default ("No Ticket Selected.");
    Class<?>[] groups() default {};
    public abstract Class<? extends Payload>[] payload() default {};
}
