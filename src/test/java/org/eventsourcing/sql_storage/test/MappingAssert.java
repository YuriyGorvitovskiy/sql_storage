package org.eventsourcing.sql_storage.test;

import static org.eventsourcing.sql_storage.test.Asserts.assertMapEquals;
import static org.eventsourcing.sql_storage.test.Asserts.context;
import static org.eventsourcing.sql_storage.test.Asserts.failMessage;
import static org.eventsourcing.sql_storage.test.SchemaAssert.assertColumnEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eventsourcing.sql_storage.mapping.MappingAttribute;
import org.eventsourcing.sql_storage.mapping.MappingEntity;
import org.eventsourcing.sql_storage.mapping.MappingModel;
import org.eventsourcing.sql_storage.mapping.MappingRelation;
import org.eventsourcing.sql_storage.mapping.MappingScalar;
import org.eventsourcing.sql_storage.mapping.MappingValue;

public class MappingAssert {

    public static void assertMappingModelEquals(MappingModel expect, MappingModel actual) {
        assertMappingModelEquals(null, expect, actual);
    }

    public static void assertMappingEntityEquals(MappingEntity expect, MappingEntity actual) {
        assertMappingEntityEquals(null, expect, actual);
    }

    public static void assertMappingAttributeEquals(MappingAttribute expect, MappingAttribute actual) {
        assertMappingAttributeEquals(null, expect, actual);
    }

    public static void assertMappingValueEquals(MappingValue expect, MappingValue actual) {
        assertMappingValueEquals(null, expect, actual);
    }

    public static void assertMappingModelEquals(String context, MappingModel expect, MappingModel actual) {
        if (expect == actual)
            return;

        final String TYPE = MappingModel.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        assertMapEquals(context(context, TYPE, "entities"),
                expect.entities,
                actual.entities,
                MappingAssert::assertMappingEntityEquals);
    }

    public static void assertMappingEntityEquals(String context, MappingEntity expect, MappingEntity actual) {
        if (expect == actual)
            return;

        final String TYPE = MappingEntity.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        assertMapEquals(context(context, TYPE, "columns"),
                expect.attributes,
                actual.attributes,
                MappingAssert::assertMappingAttributeEquals);
    }

    public static void assertMappingAttributeEquals(String context, MappingAttribute expect, MappingAttribute actual) {
        if (expect == actual)
            return;

        final String TYPE = MappingAttribute.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        if (expect.getClass() != actual.getClass())
            failMessage(context(context, TYPE, "class"), expect.getClass(), actual.getClass());

        if (expect instanceof MappingRelation) {
            assertMappingRelationEquals(context, (MappingRelation) expect, (MappingRelation) actual);
        } else if (expect instanceof MappingScalar) {
            assertMappingScalarEquals(context, (MappingScalar) expect, (MappingScalar) actual);
        } else {
            assertEquals(expect, actual, "Context: " + context(context, TYPE));
        }
    }

    private static void assertMappingRelationEquals(String context, MappingRelation expect, MappingRelation actual) {
        final String TYPE = MappingRelation.class.getSimpleName();
        assertMapEquals(context(context, TYPE, "relations"),
                expect.relations,
                actual.relations,
                MappingAssert::assertMappingValueEquals);
    }

    private static void assertMappingScalarEquals(String context, MappingScalar expect, MappingScalar actual) {
        final String TYPE = MappingScalar.class.getSimpleName();
        assertMappingValueEquals(context(context, TYPE, "relations"), expect.value, actual.value);
    }

    public static void assertMappingValueEquals(String context, MappingValue expect, MappingValue actual) {
        if (expect == actual)
            return;

        final String TYPE = MappingValue.class.getSimpleName();
        if (expect == null || actual == null)
            failMessage(context(context, TYPE), expect, actual);

        if (expect.removable != actual.removable)
            failMessage(context(context, TYPE, "removable"), expect.removable, actual.removable);

        assertColumnEquals(context(context, TYPE, "columnId"), expect.columnId, actual.columnId);
        assertColumnEquals(context(context, TYPE, "columnKey"), expect.columnKey, actual.columnKey);
        assertColumnEquals(context(context, TYPE, "columnValue"), expect.columnValue, actual.columnValue);
    }

}
