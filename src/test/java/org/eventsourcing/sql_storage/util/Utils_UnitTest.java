package org.eventsourcing.sql_storage.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Test;

public class Utils_UnitTest {

    @Test
    public void codeCoverage() {
	// Execute
	Asserts.assertPrivateConstructor(Utils.class);
    }

    @Test
    public void checkEquals() {
	// Setup
	final Object STRING1 = "Hello";
	final Object STRING2 = "World";
	final Object INTEGER = 42;

	// Execute & Verify
	assertTrue(Utils.equalsCheck(STRING1, STRING1));
	assertTrue(Utils.equalsCheck(INTEGER, INTEGER));
	assertTrue(Utils.equalsCheck(null, null));

	assertFalse(Utils.equalsCheck(STRING1, INTEGER));
	assertFalse(Utils.equalsCheck(INTEGER, STRING2));
	assertFalse(Utils.equalsCheck(null, STRING1));
	assertFalse(Utils.equalsCheck(INTEGER, null));

	assertNull(Utils.equalsCheck(STRING1, STRING2));
	assertNull(Utils.equalsCheck(STRING2, STRING1));
    }
}
