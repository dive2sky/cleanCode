package com.dive2sky.cleancode;

import java.text.ParseException;
import java.util.*;

public class Args {

    private String schema;
    private String[] args;
    private boolean valid = true;
    private Set<Character> unexpectedArguments = new TreeSet<Character>();
    private Map<Character, ArgumentMarshaller> marshallers = new HashMap<Character, ArgumentMarshaller>();
    private Set<Character> argsFound = new HashSet<Character>();
    private int currentArgument;
    private char errorArgumentId = '\0';
    private String errorParameter = "TILT";
    private ErrorCode errorCode = ErrorCode.OK;

    enum ErrorCode {
        OK,
        MISSING_STRING,
        MISSING_INTEGER,
        INVALID_INTEGER,
        UNEXPECTED_ARGUMENT
    }

    public Args(String schema, String[] args) throws ParseException {
        this.schema = schema;
        this.args = args;
        valid = parse();
    }

    private boolean parse() throws ParseException {
        if (schema.length() == 0 && args.length == 0)
            return true;
        parseSchema();
        try {
            parseArguments();
        } catch (ArgsException e) {
        }
        return valid;
    }

    private boolean parseSchema() throws ParseException {
        for (String element : schema.split(",")) {
            if (element.length() > 0) {
                String trimmedElement = element.trim();
                parseSchemaElement(trimmedElement);
            }
        }
        return true;
    }

    private void parseSchemaElement(String element) throws ParseException {
        char elementId = element.charAt(0);
        String elementTail = element.substring(1);
        validateSchemaElementId(elementId);
        if(isBooleanSchemaElement(elementTail))
            parseBooleanSchemaElement(elementId);
        else if (isStringSchemaElement(elementTail))
            parseStringSchemaElement(elementId);
        else if (isIntegerSchemaElement(elementTail))
            parseIntegerSchemaElement(elementId);
        else {
            throw new ParseException(
                    String.format("Argument: %c has invalid format : %s.",
                            elementId, elementTail), 0);
        }
    }

    private void validateSchemaElementId(char elementId) throws ParseException {
        if(!Character.isLetter(elementId)) {
            throw new ParseException(
                    "Bad character:" + elementId + "in Args format: "+ schema, 0);
        }
    }

    private void parseBooleanSchemaElement(char elementId) {
        marshallers.put(elementId, new BooleanArgumentMarshaller());
    }

    private void parseIntegerSchemaElement(char elementId) {
        marshallers.put(elementId, new IntegerArgumentMarshaller());
    }

    private void parseStringSchemaElement(char elementId) {
        marshallers.put(elementId, new StringArgumentMarshaller());
    }

    private boolean isStringSchemaElement(String elementTail) {
        return elementTail.equals("*");
    }

    private boolean isBooleanSchemaElement(String elementTail) {
        return elementTail.length() == 0;
    }

    private boolean isIntegerSchemaElement(String elementTail) {
        return elementTail.equals("#");
    }

    private boolean parseArguments() throws ArgsException {
        for(currentArgument = 0; currentArgument < args.length; currentArgument++) {
            String arg = args[currentArgument];
            parseArgument(arg);
        }
        return true;
    }

    private void parseArgument(String arg) throws ArgsException {
        if(arg.startsWith("-"))
            parseElements(arg);
    }

    private void parseElements(String arg) throws ArgsException {
        for (int i = 1; i < arg.length(); i++) {
            parseElement(arg.charAt(i));
        }
    }

    private void parseElement(char argChar) throws ArgsException {
        if (setArgument(argChar))
            argsFound.add(argChar);
        else {
            unexpectedArguments.add(argChar);
            errorCode = ErrorCode.UNEXPECTED_ARGUMENT;
            valid = false;
        }
    }

    private boolean setArgument(char argChar) throws ArgsException {
        ArgumentMarshaller am = marshallers.get(argChar);

        try {
            if (am instanceof BooleanArgumentMarshaller)
                setBooleanArg(am);
            else if (am instanceof StringArgumentMarshaller)
                setStringArg(am);
            else if (am instanceof IntegerArgumentMarshaller)
                setIntArg(am);
            else
                return false;
        } catch (ArgsException e) {
            valid = false;
            errorArgumentId = argChar;
            throw e;
        }

        return true;
    }

    private void setIntArg(ArgumentMarshaller am) throws ArgsException {
        currentArgument++;
        String parameter = null;
        try {
            parameter = args[currentArgument];
            am.set(parameter);
        } catch (ArrayIndexOutOfBoundsException e) {
            errorCode = ErrorCode.MISSING_INTEGER;
            throw new ArgsException();
        } catch (ArgsException e) {
            errorCode = ErrorCode.INVALID_INTEGER;
            errorParameter = parameter;
            throw e;
        }
    }

    private void setStringArg(ArgumentMarshaller am) throws ArgsException {
        currentArgument++;
        try {
            am.set(args[currentArgument]);
        } catch (ArrayIndexOutOfBoundsException e) {
            errorCode = ErrorCode.MISSING_STRING;
            throw new ArgsException();
        }
    }

    private void setBooleanArg(ArgumentMarshaller am) throws ArgsException {
        am.set("true");
    }

    public int cardinality() {
        return argsFound.size();
    }

    public String usage() {
        if (schema.length() > 0)
            return "-[" + schema + "]";
        else
            return "";
    }

    public String errorMessage() throws Exception {
        switch (errorCode) {
            case OK:
                throw new Exception("TILT: Should not get here.");
            case UNEXPECTED_ARGUMENT:
                return unexpectedArgumentMessage();
            case MISSING_STRING:
                return String.format("Could not find string parameter for -%c.",
                        errorArgumentId);
            case INVALID_INTEGER:
                return String.format("Argument -c% expects an integer but was '%s'",
                        errorArgumentId, errorParameter);
            case MISSING_INTEGER:
                return String.format("Could not find integer parameter for -%c.",
                        errorArgumentId);
        }
        return "";
    }

    private String unexpectedArgumentMessage() {
        StringBuffer message = new StringBuffer("Argument(s) -");
        for (char c : unexpectedArguments) {
            message.append(c);
        }
        message.append(" unexpected.");

        return message.toString();
    }

    public String getString(char arg) {
        ArgumentMarshaller am = marshallers.get(arg);
        return am == null ? "" : (String)am.get();
    }

    public int getInt(char arg) {
        ArgumentMarshaller am = marshallers.get(arg);
        return am == null ? 0 : (int)am.get();
    }

    public boolean getBoolean(char arg) {
        ArgumentMarshaller am = marshallers.get(arg);
        return am != null && (boolean)am.get();
    }

    public boolean has(char arg) {
        return  argsFound.contains(arg);
    }

    public boolean isValid() {
        return valid;
    }

    public static void main(String[] args) {
        try {
            Args arg = new Args("l,d*", args);
            boolean logging = arg.getBoolean('l');
            String strValue = arg.getString('d');
            System.out.println("logging arguments value is [" + logging +"]");
            System.out.println("string arguments value is [" + strValue+"]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
