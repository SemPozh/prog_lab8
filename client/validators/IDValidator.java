package laba8.laba8.client.validators;

import laba8.laba8.common.exeptions.InvalidObjectFieldException;

public class IDValidator extends Validator<Integer>{
    @Override
    public Integer validate(String value) throws InvalidObjectFieldException {
        if (!value.isEmpty()){
            try {
                int id = Integer.parseInt(value);
                return id;
            } catch (NumberFormatException e){
                throw new InvalidObjectFieldException("IDMustBeInteger");
            }
        } else {
            throw new InvalidObjectFieldException("IDUndefinedException");
        }
    }
}
