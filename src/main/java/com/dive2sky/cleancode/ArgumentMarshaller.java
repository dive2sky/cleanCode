package com.dive2sky.cleancode;

import java.util.Iterator;

public interface ArgumentMarshaller {
    void set(Iterator<String> currentArgument) throws ArgsException;
    Object get();
}
