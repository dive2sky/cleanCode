package com.dive2sky.cleancode;

import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

public class ArgsTest  {

    @Test
    public void testCreateWithNoSchemaOrArguments() throws Exception {
        Args args = new Args("", new String[0]);
        assertEquals(0, args.cardinality());
    }

    @Ignore
    @Test
    public void testWithNoSchemaButWithOneArgument() throws Exception {
        try {
            new Args("", new String[]{"-x"});
        } catch (ParseException e) {
            //assertEquals(ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT, e.getErrorCode());
            //assertEquals('x', e.getErrorArgumentId());
        }
    }

    @Ignore
    @Test
    public void testWithNoSchemaButWithMultipleArguments() throws Exception {
        try {
            new Args("", new String[]{"-x", "-y"});
            fail("Args constructor should hava throws exception");
        } catch (ParseException e) {
            //assertEquals(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT, e.getErrorCode());
            //assertEquals('x', e.getErrorArgumentId());
        }
    }

    @Test
    public void testNonLetterSchema() throws Exception {
        try {
            new Args("*", new String[]{});
            fail("Args constructor should have thrown exception");
        } catch (ParseException e) {
            //assertEquals(ArgsException.ErrorCode.INVALID_ARGUMENT_NAME, e.getErrorCode());
            //assertEquals('*', e.getErrorArgumentId());
        }
    }

    @Test
    public void testInvalidArgumentFormat() throws Exception {
        try {
            new Args("f~", new String[]{});
            fail("Args constructor should have throws exception");
        } catch (ParseException e) {
            //assertEquals(ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT, e.getErrorCode());
            //assertEquals('f', e.getErrorArgumentId());
        }
    }

    @Test
    public void testSimpleBooleanPresent() throws Exception {
        Args args = new Args("x", new String[]{"-x"});
        //assertEquals(1, args.nextArgument());
        assertEquals(true, args.getBoolean('x'));
    }

    @Test
    public void testUnknownBooleanIsFalse() throws Exception {
        Args args = new Args("", new String[0]);
        assertFalse(args.getBoolean('x'));
    }

    @Test
    public void testUnsetBooleanIsFalse() throws Exception {
        Args args = new Args("x", new String[0]);
        assertFalse(args.getBoolean('x'));
    }

    @Test
    public void testSimpleStringPresent() throws Exception {
        Args args = new Args("x*", new String[]{"-x", "param"});
        //assertEquals(2, args.nextArgument());
        assertTrue(args.has('x'));
        assertEquals("param", args.getString('x'));
    }

    @Test
    public void testUnknownStringIsBlank() throws Exception {
        Args args = new Args("", new String[0]);
        assertEquals("", args.getString('x'));
    }

    @Test
    public void testUnsetStringIsBlank() throws Exception {
        Args args = new Args("x*", new String[0]);
        assertEquals("", args.getString('x'));
    }

    @Ignore
    @Test
    public void testMissingStringArgument() throws Exception {
        try {
            new Args("x*", new String[]{"-x"});
            fail("Args constructor should hava throws exception");
        } catch (ParseException e) {
            //assertEquals(ArgsException.ErrorCode.MISSING_STRING, e.getErrorCode());
            //assertEquals('x', e.getErrorArgumentId());
        }
    }

    @Test
    public void testSpacesInFormat() throws Exception {
        Args args = new Args("x, y", new String[]{"-xy"});
        //assertEquals(1, args.nextArgument());
        assertTrue(args.has('x'));
        assertTrue(args.has('y'));
    }

}
