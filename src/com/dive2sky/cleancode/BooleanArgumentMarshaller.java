package com.dive2sky.cleancode;

public class BooleanArgumentMarshaller extends ArgumentMarshaller {
    private boolean booleanValue;

    @Override
    public void set(String value) {
        booleanValue = true;
    }

    @Override
    public Object get() {
        return booleanValue;
    }
}
