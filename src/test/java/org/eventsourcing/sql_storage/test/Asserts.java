package org.eventsourcing.sql_storage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eventsourcing.sql_storage.util.Helper;

public class Asserts {
    public interface ContextAssert<T> {
        public void assertFunction(String context, T expected, T actual);
    }

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

    public static <K extends Comparable<K>, E> void assertMapEquals(String context, Map<K, E> expect, Map<K, E> actual,
            ContextAssert<E> azzert) {
        if (expect == actual)
            return;

        final String TYPE = Map.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        assertSetEquals(context(context, TYPE), expect.keySet(), actual.keySet());
        List<K> keys = new ArrayList<>(expect.keySet());
        Collections.sort(keys);
        for (K key : keys) {
            azzert.assertFunction(context(context, TYPE, Objects.toString(key)), expect.get(key), actual.get(key));
        }
    }

    public static <T> void assertListEquals(String context, List<T> expect, List<T> actual, ContextAssert<T> azzert) {
        if (expect == actual)
            return;

        final String TYPE = List.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        if (expect.size() != actual.size())
            failMessage(context(context, TYPE, "size()"), expect.size(), actual.size());

        int size = expect.size();
        for (int index = 0; index < size; ++index) {
            String entryContext = context(context, TYPE, Integer.toString(index));
            azzert.assertFunction(entryContext, expect.get(index), actual.get(index));
        }
    }

    public static <T extends Comparable<T>> void assertSetEquals(String context, Set<T> expect, Set<T> actual) {
        if (expect == actual)
            return;

        final String TYPE = Set.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        List<T> missing = new ArrayList<>();
        for (T item : expect) {
            if (!actual.contains(item))
                missing.add(item);
        }

        if (!missing.isEmpty()) {
            Collections.sort(missing);
            fail("Actual " + context(context, TYPE) + " is missing entries: " + missing + ".");
        }

        List<T> unexpect = new ArrayList<>();
        for (T item : actual) {
            if (!expect.contains(item))
                unexpect.add(item);
        }
        if (!unexpect.isEmpty()) {
            Collections.sort(unexpect);
            fail("Actual " + context(context, TYPE) + " has unexpected entries: " + unexpect + ".");
        }
    }

    public static String context(String parentContext, String type) {
        return Helper.isEmpty(parentContext) ? type : parentContext;
    }

    public static String context(String parentContext, String type, String field) {
        return context(parentContext, type) + "." + field;
    }

    public static void failMessage(String context, Object expect, Object actual) {
        fail("Expected " + context + " is: " + expect + ", but actual was: " + actual + ".");
    }

}
