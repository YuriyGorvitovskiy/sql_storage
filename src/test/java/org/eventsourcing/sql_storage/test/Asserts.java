package org.eventsourcing.sql_storage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Asserts {
    public static <T extends Enum<T>> void assertEnum(Class<T> subject) {
	// To pass 100% Code Coverage for enumeration
	try {
	    assertTrue(subject.isEnum());
	    T[] e = subject.getEnumConstants();
	    Method m = subject.getMethod("valueOf", String.class);
	    for (int i = 0; i < e.length; ++i) {
		assertSame(e[0], m.invoke(null, e[0].toString()));
	    }
	} catch (AssertionError ex) {
	    throw ex;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    fail(ex.getMessage());
	}
    }

    public static void assertPrivateConstructor(Class<?> clazz) {
	// Add test and code coverage for Private constructor
	try {
	    Constructor<?> constructor = clazz.getDeclaredConstructor();
	    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	    constructor.setAccessible(true);
	    constructor.newInstance();
	} catch (AssertionError ex) {
	    throw ex;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    fail(ex.getMessage());
	}
    }

    public static void assertEquality(Object... equalsObjects) {
	for (Object a : equalsObjects) {
	    for (Object b : equalsObjects) {
		assertEquals(a, b);
		assertEquals("HashCode should be the same for: " + a + " and " + b, a.hashCode(), b.hashCode());
	    }
	}
    }

    public static void assertInequality(Object... equalsObjects) {
	for (Object a : equalsObjects) {
	    for (Object b : equalsObjects) {
		if (a != b) {
		    assertNotEquals(a, b);
		}
	    }
	}
    }
}
