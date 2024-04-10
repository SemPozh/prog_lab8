package laba6.client.validators;

import laba6.common.data.Address;
import laba6.common.exeptions.InvalidObjectFieldException;

public class ZipCodeValidator extends Validator<Address>{
    @Override
    public Address validate(String value) throws InvalidObjectFieldException {
        if (value.isEmpty()){
            return null;
        } else {
            return new Address(value);
        }
    }
}