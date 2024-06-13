package laba8.laba8.client.validators;

import laba8.laba8.common.exeptions.InvalidObjectFieldException;

public class EmployeeCountValidator extends Validator<Integer>{
    @Override
    public Integer validate(String value) throws InvalidObjectFieldException {
        try{
            int employeeCount = Integer.parseInt(value);
            if (employeeCount <= 0){
                throw new InvalidObjectFieldException("EmployeeCountSignException");
            }
            return employeeCount;
        } catch (NumberFormatException e){
            throw new InvalidObjectFieldException("EmployeeCountMustBeInteger!");
        }
    }
}