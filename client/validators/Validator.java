package laba8.laba8.client.validators;

import laba8.laba8.common.exeptions.InvalidObjectFieldException;

public abstract class Validator<T> {
    public abstract T validate(String value) throws InvalidObjectFieldException;
}
