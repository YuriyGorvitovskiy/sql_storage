package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.ValueType.INTEGER;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_LIST;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_MAP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Model_UnitTest {
    final String ENTITY_NAME1 = "Entity";
    final String ENTITY_NAME2 = "Hello";
    final String ENTITY_NAME3 = "World";

    final String ATTR_NAME1 = "first";
    final String ATTR_NAME2 = "second";
    final String ATTR_NAME3 = "third";

    final Model MODEL1 = new Model.Builder()
        .type((t) -> t
            .name(ENTITY_NAME1)
            .attribute((a) -> a
                .name(ATTR_NAME1)
                .type(INTEGER))
            .attribute((a) -> a
                .name(ATTR_NAME2)
                .type(REFERENCE_MAP)
                .relation((r) -> r
                    .target(ENTITY_NAME2)
                    .reverse(ATTR_NAME1))
                .relation((r) -> r
                    .target(ENTITY_NAME3)
                    .reverse(ATTR_NAME1))))
        .type((t) -> t
            .name(ENTITY_NAME2)
            .attribute(ATTR_NAME1, (a) -> a
                .type(REFERENCE_LIST)
                .relation((r) -> r
                    .target(ENTITY_NAME1)
                    .reverse(ATTR_NAME2))))
        .type((t) -> t
            .name(ENTITY_NAME3)
            .attribute(ATTR_NAME1, (a) -> a
                .type(REFERENCE)
                .relation((r) -> r
                    .target(ENTITY_NAME1)
                    .reverse(ATTR_NAME2))))
        .build();

    final Model MODEL2 = new Model.Builder()
        .type(ENTITY_NAME1, (t) -> t
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME2, REFERENCE_MAP, (a) -> a
                .relation(ENTITY_NAME2, ATTR_NAME1)
                .relation(ENTITY_NAME3, ATTR_NAME1)))
        .type(ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE_LIST, ENTITY_NAME1, ATTR_NAME2))
        .type(ENTITY_NAME3, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE, ENTITY_NAME1, ATTR_NAME2))
        .build();

    final Model MODEL3 = new Model.Builder()
        .type(ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE_LIST, ENTITY_NAME3, ATTR_NAME1))
        .type(ENTITY_NAME3, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE, ENTITY_NAME2, ATTR_NAME1))
        .build();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void builder_direct() {
        // Execute
        Model model = new Model.Builder()
            .type(ENTITY_NAME1, c -> {})
            .type(ENTITY_NAME2, c -> {})
            .type(ENTITY_NAME3, c -> {})
            .build();

        // Verify
        assertSame(ENTITY_NAME1, model.getEntityType(ENTITY_NAME1).name);
        assertSame(ENTITY_NAME2, model.getEntityType(ENTITY_NAME2).name);
        assertSame(ENTITY_NAME3, model.getEntityType(ENTITY_NAME3).name);
        assertEquals(3, model.entityTypes.size());
    }

    @Test
    public void builder_dupliocate_type() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("duplicate Entity Type names");

        // Execute
        new Model.Builder()
            .type(ENTITY_NAME1, (c) -> {})
            .type(ENTITY_NAME2, (c) -> {})
            .type(ENTITY_NAME2, (c) -> {})
            .build();
    }

    @Test
    public void equals_and_hash() {
        // Execute & Verify
        Asserts.assertEquality(MODEL1, MODEL2);

        Asserts.assertInequality(null, MODEL1, MODEL3);
    }
}
