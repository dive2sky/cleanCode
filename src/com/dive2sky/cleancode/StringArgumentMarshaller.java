package com.dive2sky.cleancode;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StringArgumentMarshaller implements ArgumentMarshaller {

    private String stringValue = "";

    public void set(String value) {
        stringValue = value;
    }

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        try {
            set(currentArgument.next());
        } catch (NoSuchElementException e) {
            Args.errorCode = Args.ErrorCode.MISSING_STRING;
            throw new ArgsException();
        }
    }

    @Override
    public Object get() {
        return stringValue;
    }
}
