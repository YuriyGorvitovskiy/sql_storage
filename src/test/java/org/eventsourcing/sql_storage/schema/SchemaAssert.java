package org.eventsourcing.sql_storage.schema;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eventsourcing.sql_storage.util.Helper;

public class SchemaAssert {

    public interface ContextAssert<T> {
        public void assertFunction(String context, T expected, T actual);
    }

    public static void assertSchemaEquals(Schema expect, Schema actual) {
        assertSchemaEquals(null, expect, actual);
    }

    public static void assertTableEquals(Table expect, Table actual) {
        assertTableEquals(null, expect, actual);
    }

    public static void assertIndexEquals(Index expect, Index actual) {
        assertIndexEquals(null, expect, actual);
    }

    public static void assertColumnEquals(Column expect, Column actual) {
        assertColumnEquals(null, expect, actual);
    }

    public static void assertDataTypeEquals(DataType expect, DataType actual) {
        assertDataTypeEquals(null, expect, actual);
    }

    public static void assertSchemaEquals(String context, Schema expect, Schema actual) {
        if (expect == actual)
            return;

        final String TYPE = Schema.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        assertMapEquals(context(context, TYPE, "tables"),
            expect.tables,
            actual.tables,
            SchemaAssert::assertTableEquals);
    }

    public static void assertTableEquals(String context, Table expect, Table actual) {
        if (expect == actual)
            return;

        final String TYPE = Table.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        if (!Objects.equals(expect.name, actual.name))
            failMessage(context(context, TYPE, "name"), expect.name, actual.name);

        assertMapEquals(context(context, TYPE, "columns"),
            expect.columns,
            actual.columns,
            SchemaAssert::assertColumnEquals);

        assertMapEquals(context(context, TYPE, "indexes"),
            expect.indexes,
            actual.indexes,
            SchemaAssert::assertIndexEquals);
    }

    public static void assertIndexEquals(String context, Index expect, Index actual) {
        if (expect == actual)
            return;

        final String TYPE = Index.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        if (!Objects.equals(expect.name, actual.name))
            failMessage(context(context, TYPE, "name"), expect.name, actual.name);

        if (expect.primary != actual.primary)
            failMessage(context(context, TYPE, "primary"), expect.primary, actual.primary);

        assertListEquals(context(context, TYPE, "columns"),
            expect.columns,
            actual.columns,
            SchemaAssert::assertColumnEquals);
    }

    public static void assertColumnEquals(String context, Column expect, Column actual) {
        if (expect == actual)
            return;

        final String TYPE = Column.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        if (!Objects.equals(expect.name, actual.name))
            failMessage(context(context, TYPE, "name"), expect.name, actual.name);

        assertDataTypeEquals(context(context, TYPE, "type"), expect.type, actual.type);
    }

    public static void assertDataTypeEquals(String context, DataType expect, DataType actual) {
        if (expect == actual)
            return;

        final String TYPE = DataType.class.getSimpleName();
        failMessage(context(context, TYPE), expect, actual);
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
