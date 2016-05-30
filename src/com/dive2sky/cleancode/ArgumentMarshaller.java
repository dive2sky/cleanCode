package com.dive2sky.cleancode;

public abstract class ArgumentMarshaller {

    public abstract void set(String value) throws ArgsException;

    public abstract Object get();

}
