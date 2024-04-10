package laba6.client.validators;

import laba6.common.exeptions.InvalidObjectFieldException;

public class EmployeeCountValidator extends Validator<Integer>{
    @Override
    public Integer validate(String value) throws InvalidObjectFieldException {
        try{
            int employeeCount = Integer.parseInt(value);
            if (employeeCount <= 0){
                throw new InvalidObjectFieldException("Employee count must be > 0");
            }
            return employeeCount;
        } catch (NumberFormatException e){
            throw new InvalidObjectFieldException("Employee count defined incorrectly!");
        }
    }
}