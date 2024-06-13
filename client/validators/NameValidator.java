package laba8.laba8.client.validators;

import laba8.laba8.common.exeptions.InvalidObjectFieldException;

public class NameValidator extends Validator<String>{
    @Override
    public String validate(String value) throws InvalidObjectFieldException {
        if (value.isEmpty()){
            throw new InvalidObjectFieldException("NameCantBeEmptyException");
        }
        return value;
    }
}