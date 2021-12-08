package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateValidation implements ConstraintValidator<EventDate, String> {

    @Override
    public void initialize(EventDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {

        // creating local time from the date provided by user for the event.
        LocalDateTime inputDateTime = LocalDateTime.parse(date+"T00:00:00.000");
        LocalDateTime currentDateTime = LocalDateTime.now();

        // comparing date provided by user with the current date.
        if (inputDateTime.isAfter(currentDateTime) || inputDateTime.isEqual(currentDateTime)) {
            return true;
        } else {
            return false;
        }
    }
}
