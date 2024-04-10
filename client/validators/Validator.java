package laba6.client.validators;

import laba6.common.exeptions.InvalidObjectFieldException;

public abstract class Validator<T> {
    public abstract T validate(String value) throws InvalidObjectFieldException;
}
