package laba6.client.validators;

import laba6.common.data.OrganizationType;
import laba6.common.exeptions.InvalidObjectFieldException;

public class OrganizationTypeValidator extends Validator<OrganizationType>{
    @Override
    public OrganizationType validate(String value) throws InvalidObjectFieldException {
        try{
            return OrganizationType.valueOf(value);
        } catch (IllegalArgumentException e){
            throw new InvalidObjectFieldException("There are no such organization type!");
        }

    }
}
