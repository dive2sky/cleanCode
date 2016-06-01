package com.dive2sky.cleancode;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.dive2sky.cleancode.Args.errorCode;
import static com.dive2sky.cleancode.Args.errorParameter;

public class IntegerArgumentMarshaller implements ArgumentMarshaller {

    private int intValue;

    public void set(String value) throws ArgsException {
        try {
            intValue = new Integer(value);
        } catch (NumberFormatException e) {
            throw new ArgsException();
        }
    }

    public void set(Iterator<String> currentArgument) throws ArgsException {
        String parameter = null;
        try {
            parameter = currentArgument.next();
            set(parameter);
        } catch (NoSuchElementException e) {
            errorCode = Args.ErrorCode.MISSING_INTEGER;
            throw new ArgsException();
        } catch (ArgsException e) {
            errorCode = Args.ErrorCode.INVALID_INTEGER;
            errorParameter = parameter;
            throw e;
        }
    }

    @Override
    public Object get() {
        return intValue;
    }
}
