package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;

public class TransferTicketIdListValidation implements ConstraintValidator<TransferTicketIdList, ArrayList<Integer>> {

    @Override
    public void initialize(TransferTicketIdList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ArrayList<Integer> ticketIdList, ConstraintValidatorContext constraintValidatorContext) {
        if (ticketIdList.isEmpty()) {
            return false;
        }
        return true;
    }

}
