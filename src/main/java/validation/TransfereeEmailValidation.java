package validation;

import model.User;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.SQLException;
import static jdbc.JDBCUserTableOperations.findUserFromUserInfoByEmailId;

/**
 * constraintValidator class for transferee field of Event class.
 * @author nilimajha
 */
public class TransfereeEmailValidation implements ConstraintValidator<TransfereeEmail, String> {

    /**
     * Initializes the validator in preparation for isValid().
     * @param constraintAnnotation
     */
    @Override
    public void initialize(TransfereeEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * validate emailId belongs to a valid user.
     * @param s
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        User user = new User();
        try {
            user = findUserFromUserInfoByEmailId(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user.isValid()) {
            return true;
        } else {
            return false;
        }
    }
}
