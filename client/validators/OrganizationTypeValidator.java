package laba8.laba8.client.validators;

import laba8.laba8.common.data.OrganizationType;
import laba8.laba8.common.exeptions.InvalidObjectFieldException;

public class OrganizationTypeValidator extends Validator<OrganizationType>{
    @Override
    public OrganizationType validate(String value) throws InvalidObjectFieldException {
        try{
            return OrganizationType.fromString(value);
        } catch (IllegalArgumentException e){
            throw new InvalidObjectFieldException("NoSuchOrganizationType");
        }

    }
}
