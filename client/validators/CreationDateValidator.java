package laba6.client.validators;

import laba6.common.data.OrganizationType;
import laba6.common.exeptions.InvalidObjectFieldException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreationDateValidator extends Validator<ZonedDateTime> {
    @Override
    public ZonedDateTime validate(String value) throws InvalidObjectFieldException {
        try{
            return ZonedDateTime.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e){
            throw new InvalidObjectFieldException("Invalid creation date");
        }
    }
}
