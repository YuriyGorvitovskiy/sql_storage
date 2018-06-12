package org.eventsourcing.sql_storage.test;

import static org.eventsourcing.sql_storage.test.Asserts.assertListEquals;
import static org.eventsourcing.sql_storage.test.Asserts.assertMapEquals;
import static org.eventsourcing.sql_storage.test.Asserts.context;
import static org.eventsourcing.sql_storage.test.Asserts.failMessage;

import java.util.Objects;

import org.eventsourcing.sql_storage.schema.Column;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Index;
import org.eventsourcing.sql_storage.schema.Schema;
import org.eventsourcing.sql_storage.schema.Table;

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

}
