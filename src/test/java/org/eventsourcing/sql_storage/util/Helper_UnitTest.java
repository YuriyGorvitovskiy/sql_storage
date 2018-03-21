package org.eventsourcing.sql_storage.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Test;

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
    public void a() {
        // Execute & Verify
        assertTrue(Helper.isEmpty(null));
        assertTrue(Helper.isEmpty(""));

        assertFalse(Helper.isEmpty("abc"));
        assertFalse(Helper.isEmpty(" "));
    }
}
