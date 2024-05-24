package laba7.client.validators;

import laba7.common.exeptions.InvalidObjectFieldException;

public class NameValidator extends Validator<String>{
    @Override
    public String validate(String value) throws InvalidObjectFieldException {
        if (value.isEmpty()){
            throw new InvalidObjectFieldException("Name can't be empty");
        }
        return value;
    }
}