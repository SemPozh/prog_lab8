package laba7.client.validators;

import laba7.common.exeptions.InvalidObjectFieldException;

public class IDValidator extends Validator<Integer>{
    @Override
    public Integer validate(String value) throws InvalidObjectFieldException {
        if (!value.isEmpty()){
            try {
                int id = Integer.parseInt(value);
                return id;
            } catch (NumberFormatException e){
                throw new InvalidObjectFieldException("ID defined incorrectly!");
            }
        } else {
            throw new InvalidObjectFieldException("ID must be defined!");
        }
    }
}
