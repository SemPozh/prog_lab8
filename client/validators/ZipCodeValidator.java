package laba8.laba8.client.validators;

import laba8.laba8.common.data.Address;
import laba8.laba8.common.exeptions.InvalidObjectFieldException;

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