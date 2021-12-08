package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class UserBirthDateValidation implements ConstraintValidator<UserBirthDate, String> {

    @Override
    public void initialize(UserBirthDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {

        // creating local time from the date provided by user.
        LocalDateTime inputDateTime = LocalDateTime.parse(date+"T23:59:59.000");
        LocalDateTime currentDateTime = LocalDateTime.now();

        // comparing date provided by user with the current date.
        if (inputDateTime.isBefore(currentDateTime)) {
            return true;
        } else {
            return false;
        }
    }
}
