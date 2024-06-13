package laba8.laba8.client.validators;


import laba8.laba8.common.exeptions.InvalidObjectFieldException;

public class AnnualTurnoverValidator extends Validator<Integer>{
    @Override
    public Integer validate(String value) throws InvalidObjectFieldException {
        if (!value.isEmpty()){
            try {
                int annualTurnover = Integer.parseInt(value);
                if (annualTurnover <= 0){
                    throw new InvalidObjectFieldException("AnnualTurnoverSignException");
                }
                return annualTurnover;
            } catch (NumberFormatException e){
                throw new InvalidObjectFieldException("AnnualTurnoverMustBeInteger");
            }
        } else {
            return null;
        }
    }
}