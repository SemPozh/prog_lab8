package laba7.client.validators;

import laba7.common.data.OrganizationType;
import laba7.common.exeptions.InvalidObjectFieldException;

public class OrganizationTypeValidator extends Validator<OrganizationType>{
    @Override
    public OrganizationType validate(String value) throws InvalidObjectFieldException {
        try{
            return OrganizationType.fromString(value);
        } catch (IllegalArgumentException e){
            throw new InvalidObjectFieldException(e.getMessage());
        }

    }
}
