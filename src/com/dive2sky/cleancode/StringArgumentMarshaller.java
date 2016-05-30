package com.dive2sky.cleancode;

public class StringArgumentMarshaller extends ArgumentMarshaller {

    private String stringValue = "";

    @Override
    public void set(String value) {
        stringValue = value;
    }

    @Override
    public Object get() {
        return stringValue;
    }
}
