package validation;

import jdbc.JDBCConnectionPool;
import model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.SQLException;

public class TransfereeEmailValidation implements ConstraintValidator<TransfereeEmail, String> {

    @Override
    public void initialize(TransfereeEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        User user = new User();
        try {
            user = JDBCConnectionPool.findUserFromUserInfoByEmailId(s);
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
