package com.dive2sky.cleancode;

import java.util.Iterator;

public class BooleanArgumentMarshaller implements ArgumentMarshaller {
    private boolean booleanValue;

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        booleanValue = true;
    }

    @Override
    public Object get() {
        return booleanValue;
    }
}
