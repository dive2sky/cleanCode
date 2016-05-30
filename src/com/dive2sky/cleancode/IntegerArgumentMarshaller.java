package com.dive2sky.cleancode;

public class IntegerArgumentMarshaller extends ArgumentMarshaller {

    private int intValue;

    @Override
    public void set(String value) throws ArgsException {
        try {
            intValue = new Integer(value);
        } catch (NumberFormatException e) {
            throw new ArgsException();
        }
    }

    @Override
    public Object get() {
        return intValue;
    }
}
