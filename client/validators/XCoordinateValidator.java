package laba8.laba8.client.validators;

import laba8.laba8.common.exeptions.InvalidObjectFieldException;

public class XCoordinateValidator extends Validator<Integer> {
    @Override
    public Integer validate(String value) throws InvalidObjectFieldException {
        try{
            int x = Integer.parseInt(value);
            if (x<=-393){
                throw new InvalidObjectFieldException("XCoordinateLimitsException");
            }
            return x;
        } catch (NumberFormatException e){
            throw new InvalidObjectFieldException("XCoordinateMustBeInteger");
        }


    }
}