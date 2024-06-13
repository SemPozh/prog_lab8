package laba8.laba8.client.validators;

import laba8.laba8.common.data.OrganizationType;
import laba8.laba8.common.exeptions.InvalidObjectFieldException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreationDateValidator extends Validator<ZonedDateTime> {
    @Override
    public ZonedDateTime validate(String value) throws InvalidObjectFieldException {
        try{
            LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            return date.atStartOfDay(ZoneId.systemDefault());
        } catch (DateTimeParseException e){
            throw new InvalidObjectFieldException("InvalidCreationDate");
        }
    }
}
