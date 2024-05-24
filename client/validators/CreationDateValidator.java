package laba7.client.validators;

import laba7.common.data.OrganizationType;
import laba7.common.exeptions.InvalidObjectFieldException;

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
            System.out.println(e);
            throw new InvalidObjectFieldException("Invalid creation date");
        }
    }
}
