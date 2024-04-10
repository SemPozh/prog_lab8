package laba6.client.validators;


import laba6.common.exeptions.InvalidObjectFieldException;

public class AnnualTurnoverValidator extends Validator<Integer>{
    @Override
    public Integer validate(String value) throws InvalidObjectFieldException {
        if (!value.isEmpty()){
            try {
                int annualTurnover = Integer.parseInt(value);
                if (annualTurnover <= 0){
                    throw new InvalidObjectFieldException("Annual turnover must be > 0");
                }
                return annualTurnover;
            } catch (NumberFormatException e){
                throw new InvalidObjectFieldException("Annual turnover defined incorrectly!");
            }
        } else {
            return null;
        }
    }
}