package laba7.client.validators;

import laba7.common.exeptions.InvalidObjectFieldException;

public class YCoordinateValidator extends Validator<Double>{
    @Override
    public Double validate(String value) throws InvalidObjectFieldException {
        try{
            double y = Double.parseDouble(value);
            if (y>518){
                throw new InvalidObjectFieldException("Y-coordinate must be <= 518");
            }
            return y;
        } catch (NumberFormatException e){
            throw new InvalidObjectFieldException("Y-coordinate defined incorrectly");
        }
    }
}