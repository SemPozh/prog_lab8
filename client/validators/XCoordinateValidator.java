package laba6.client.validators;

import laba6.common.exeptions.InvalidObjectFieldException;

public class XCoordinateValidator extends Validator<Integer> {
    @Override
    public Integer validate(String value) throws InvalidObjectFieldException {
        try{
            int x = Integer.parseInt(value);
            if (x<=-393){
                throw new InvalidObjectFieldException("X-coordinate must be > -393");
            }
            return x;
        } catch (NumberFormatException e){
            throw new InvalidObjectFieldException("X-coordinate defined incorrectly");
        }


    }
}