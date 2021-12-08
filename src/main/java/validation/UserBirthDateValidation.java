package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

/**
 * constraintValidator class for dateOfBirth field of user class.
 * @author nilimajha
 */
public class UserBirthDateValidation implements ConstraintValidator<UserBirthDate, String> {

    /**
     * Initializes the validator in preparation for isValid().
     * @param constraintAnnotation
     */
    @Override
    public void initialize(UserBirthDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * validate if the date entered is past date.
     * @param date
     * @param constraintValidatorContext
     * @return true or false
     */
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
