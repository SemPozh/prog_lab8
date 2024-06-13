package laba8.laba8.client.validators;

import laba8.laba8.common.exeptions.InvalidObjectFieldException;

public class YCoordinateValidator extends Validator<Double>{
    @Override
    public Double validate(String value) throws InvalidObjectFieldException {
        try{
            double y = Double.parseDouble(value);
            if (y>518){
                throw new InvalidObjectFieldException("YCoordinateLimitsException");
            }
            return y;
        } catch (NumberFormatException e){
            throw new InvalidObjectFieldException("УCoordinateMustBeDouble");
        }
    }
}