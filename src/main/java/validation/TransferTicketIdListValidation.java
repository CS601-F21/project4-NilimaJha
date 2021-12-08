package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;

/**
 * constraintValidator class for transferTicketIdList field of TicketTransfer class.
 * @author nilimajha
 */
public class TransferTicketIdListValidation implements ConstraintValidator<TransferTicketIdList, ArrayList<Integer>> {

    /**
     * Initializes the validator in preparation for isValid().
     * @param constraintAnnotation
     */
    @Override
    public void initialize(TransferTicketIdList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validate is the list is not empty.
     * @param ticketIdList
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(ArrayList<Integer> ticketIdList, ConstraintValidatorContext constraintValidatorContext) {
        if (ticketIdList.isEmpty()) {
            return false;
        }
        return true;
    }

}
