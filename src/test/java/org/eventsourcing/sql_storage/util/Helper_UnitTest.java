package org.eventsourcing.sql_storage.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Helper_UnitTest {
    public static final String ONE   = "1";
    public static final String TWO   = "II";
    public static final String THREE = "three";
    public static final String NULL  = null;

    @SuppressWarnings("unused")
    private static final String   PRIVATE    = "private";
    protected static final String PROTECTED  = "protected";
    static final String           PACKAGE    = "packages";
    public static String          MUTABLE    = "mutable";
    public final String           DYNAMIC    = "dynamic";
    public static final Integer   WRONG_TYPE = 42;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void codeCoverage() {
        // Execute
        Asserts.assertPrivateConstructor(Helper.class);
    }

    @Test
    public void valuesOfPublicStaticFinalFields() throws Exception {
        // Execute
        List<String> values = Helper.valuesOfPublicStaticFinalFields(String.class, Helper_UnitTest.class);

        // Verify
        assertTrue(values.contains(ONE));
        assertTrue(values.contains(TWO));
        assertTrue(values.contains(THREE));
        assertEquals(3, values.size());
    }

    @Test
    public void getStaticField() throws Exception {
        // Setup
        Class<?> CLASS = Helper_UnitTest.class;

        // Execute & Verify
        assertEquals(ONE, Helper.getStaticField(String.class, CLASS.getDeclaredField("ONE")));
        assertEquals(PROTECTED, Helper.getStaticField(String.class, CLASS.getDeclaredField("PROTECTED")));
        assertEquals(PACKAGE, Helper.getStaticField(String.class, CLASS.getDeclaredField("PACKAGE")));

        assertNull(Helper.getStaticField(String.class, CLASS.getDeclaredField("PRIVATE")));
        assertNull(Helper.getStaticField(String.class, CLASS.getDeclaredField("WRONG_TYPE")));
        assertNull(Helper.getStaticField(String.class, CLASS.getDeclaredField("NULL")));
        assertNull(Helper.getStaticField(String.class, CLASS.getDeclaredField("DYNAMIC")));
    }

    @Test
    public void isEmpty() {
        // Execute & Verify
        assertTrue(Helper.isEmpty(null));
        assertTrue(Helper.isEmpty(""));

        assertFalse(Helper.isEmpty("abc"));
        assertFalse(Helper.isEmpty(" "));
    }

    @Test
    public void resourceAsString() {
        // Execute & Verify
        assertEquals("Hello world!", Helper.resourceAsString(Helper.class, "resourceAsString.txt"));
    }

    @Test
    public void resourceAsLines() {
        // Setup
        List<String> expected = Arrays.asList("  Line1", "", " Line2");

        // Execute
        List<String> actual = Helper.resourceAsLines(Helper.class, "resourceAsLines.txt");

        // Verify
        assertEquals(expected, actual);
    }

    @Test
    public void resourceAsDictionary() {
        // Setup
        Set<String> expected = new HashSet<>();
        expected.add("line1");
        expected.add("line2");

        // Execute
        Set<String> actual = Helper.resourceAsDictionary(Helper.class, "resourceAsLines.txt");

        // Verify
        assertEquals(expected, actual);
    }

    @Test
    public void scanResource_success() {
        // Setup
        final String SUCCESS = "success";

        // Execute
        String actual = Helper.scanResource(Helper.class, "resourceAsString.txt", (s) -> {
            assertEquals(Scanner.class, s.getClass());
            return SUCCESS;
        });

        // Verify
        assertEquals(SUCCESS, actual);
    }

    @Test
    public void scanResource_noResource() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("non-existing");

        // Execute
        Helper.scanResource(Helper.class, "non-existing", (s) -> null);
    }

    @Test
    public void processResource_openSuccess_processSuccess_closeSuccess() {
        // Setup
        final String SUCCESS = "success";
        final String FAILURE = "failure";
        final AutoCloseable RESOURCE = () -> {};

        // Execute
        final String actual = Helper.processResource(
            () -> RESOURCE,
            (r) -> {
                assertSame(RESOURCE, r);
                return SUCCESS;
            },
            (e) -> FAILURE);
        ;

        // Verify
        assertSame(SUCCESS, actual);
    }

    @Test
    public void processResource_openNull_processSuccess_closeNon() {
        // Setup
        final String SUCCESS = "success";
        final String FAILURE = "failure";

        // Execute
        final String actual = Helper.processResource(
            () -> null,
            (r) -> SUCCESS,
            (e) -> FAILURE);

        // Verify
        assertSame(SUCCESS, actual);
    }

    @Test
    public void processResource_openNull_processFailure_closeNon() {
        // Setup
        final String FAILURE = "failure";

        // Execute
        final String actual = Helper.processResource(
            () -> null,
            (r) -> {
                throw new IndexOutOfBoundsException();
            },
            (e) -> FAILURE);

        // Verify
        assertSame(FAILURE, actual);
    }

    @Test
    public void processResource_openFailure_processSuccess_closeSuccess() {
        // Setup
        final String SUCCESS = "success";
        final String FAILURE = "failure";

        // Execute
        final String actual = Helper.processResource(
            () -> {
                throw new Exception();
            },
            (r) -> SUCCESS,
            (e) -> FAILURE);

        // Verify
        assertSame(FAILURE, actual);
    }

    @Test
    public void processResource_openSuccess_processFailure_closeSuccess() {
        // Setup
        @SuppressWarnings("resource")
        final AutoCloseable RESOURCE = () -> {};
        final String FAILURE = "failure";

        // Execute
        Helper.processResource(
            () -> RESOURCE,
            (r) -> {
                throw new IndexOutOfBoundsException();
            },
            (e) -> FAILURE);
    }

    @Test
    public void processResource_openSuccess_processSuccess_closeFailure() {
        // Setup
        @SuppressWarnings("resource")
        final AutoCloseable RESOURCE = () -> {
            throw new IndexOutOfBoundsException();
        };
        final String SUCCESS = "success";
        final String FAILURE = "failure";

        // Execute
        String actual = Helper.processResource(
            () -> RESOURCE,
            (r) -> SUCCESS,
            (e) -> FAILURE);

        // Verify
        assertSame(FAILURE, actual);
    }

    @Test
    public void processResource_openSuccess_processFailure_closeFailure() {
        // Setup
        @SuppressWarnings("resource")
        final AutoCloseable RESOURCE = () -> {
            throw new IndexOutOfBoundsException();
        };
        final String FAILURE = "failure";

        // Execute
        String actual = Helper.processResource(
            () -> RESOURCE,
            (r) -> {
                throw new IndexOutOfBoundsException();
            },
            (e) -> FAILURE);

        // Verify
        assertSame(FAILURE, actual);
    }
}
