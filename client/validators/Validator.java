package laba7.client.validators;

import laba7.common.exeptions.InvalidObjectFieldException;

public abstract class Validator<T> {
    public abstract T validate(String value) throws InvalidObjectFieldException;
}
