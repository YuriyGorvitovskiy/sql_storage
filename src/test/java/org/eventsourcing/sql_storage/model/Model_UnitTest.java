package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.ValueType.INTEGER;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_LIST;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_MAP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class Model_UnitTest {
    static final long   TYPE_ID1     = 1;
    static final long   TYPE_ID2     = 2;
    static final long   TYPE_ID3     = 3;

    static final String ENTITY_NAME1 = "Entity";
    static final String ENTITY_NAME2 = "Hello";
    static final String ENTITY_NAME3 = "World";

    static final String ATTR_NAME1   = "first";
    static final String ATTR_NAME2   = "second";
    static final String ATTR_NAME3   = "third";

    final Model         MODEL1       = new Model.Builder()
        .type((t) -> t
            .typeId(TYPE_ID1)
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
            .typeId(TYPE_ID2)
            .name(ENTITY_NAME2)
            .attribute(ATTR_NAME1,
                    (a) -> a
                        .type(REFERENCE_LIST)
                        .relation((r) -> r
                            .target(ENTITY_NAME1)
                            .reverse(ATTR_NAME2))))
        .type((t) -> t
            .typeId(TYPE_ID3)
            .name(ENTITY_NAME3)
            .attribute(ATTR_NAME1,
                    (a) -> a
                        .type(REFERENCE_LIST)
                        .relation((r) -> r
                            .target(ENTITY_NAME1)
                            .reverse(ATTR_NAME2))))
        .build();

    final Model         MODEL2       = new Model.Builder()
        .type(TYPE_ID1,
                ENTITY_NAME1,
                (t) -> t
                    .attribute(ATTR_NAME1, INTEGER)
                    .attribute(
                            ATTR_NAME2,
                            REFERENCE_MAP,
                            (a) -> a
                                .relation(ENTITY_NAME2, ATTR_NAME1)
                                .relation(ENTITY_NAME3, ATTR_NAME1)))
        .type(TYPE_ID2,
                ENTITY_NAME2,
                t -> t.attribute(ATTR_NAME1, REFERENCE_LIST, ENTITY_NAME1, ATTR_NAME2))
        .type(TYPE_ID3,
                ENTITY_NAME3,
                t -> t.attribute(ATTR_NAME1, REFERENCE_LIST, ENTITY_NAME1, ATTR_NAME2))
        .build();

    final Model         MODEL3       = new Model.Builder()
        .type(TYPE_ID2,
                ENTITY_NAME2,
                t -> t.attribute(ATTR_NAME1, REFERENCE_LIST, ENTITY_NAME3, ATTR_NAME1))
        .type(TYPE_ID3,
                ENTITY_NAME3,
                t -> t.attribute(ATTR_NAME1, REFERENCE_LIST, ENTITY_NAME2, ATTR_NAME1))
        .build();

    @Test
    public void builder_direct() {
        // Execute
        Model model = new Model.Builder()
            .type(TYPE_ID1,
                    ENTITY_NAME1,
                    c -> c.attribute(ATTR_NAME1, INTEGER))
            .type(TYPE_ID2, ENTITY_NAME2, c -> {})
            .type(TYPE_ID3, ENTITY_NAME3, c -> {})
            .build();

        // Verify
        assertSame(ENTITY_NAME1, model.getEntityType(ENTITY_NAME1).name);
        assertSame(ENTITY_NAME2, model.getEntityType(ENTITY_NAME2).name);
        assertSame(ENTITY_NAME3, model.getEntityType(ENTITY_NAME3).name);
        assertEquals(3, model.entityTypes.size());

        assertEquals(
                model.getEntityType(ENTITY_NAME1).getAttribute(ATTR_NAME1),
                model.getAttribute(ENTITY_NAME1, ATTR_NAME1));

        assertNull(model.getAttribute(ENTITY_NAME1, "Not Exists"));
        assertNull(model.getAttribute("Not Exists", ATTR_NAME1));
    }

    @Test
    public void builder_duplicate_type() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Model.Builder()
                    .type(TYPE_ID1, ENTITY_NAME1, (c) -> {})
                    .type(TYPE_ID2, ENTITY_NAME2, (c) -> {})
                    .type(TYPE_ID3, ENTITY_NAME2, (c) -> {})
                    .build());

        // Verify
        assertTrue(thrown.getMessage().contains("duplicate Entity Type names"));
    }

    @Test
    public void builder_duplicate_typeId() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Model.Builder()
                    .type(TYPE_ID1, ENTITY_NAME1, t -> {})
                    .type(TYPE_ID2, ENTITY_NAME2, t -> {})
                    .type(TYPE_ID2, ENTITY_NAME3, t -> {})
                    .build());

        // Verify
        assertTrue(thrown.getMessage().contains("duplicate Entity Type Ids"));
    }

    @Test
    public void builder_no_reverse() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Model.Builder()
                    .type(TYPE_ID1,
                            ENTITY_NAME1,
                            t -> t.attribute(
                                    ATTR_NAME1,
                                    REFERENCE,
                                    a -> a.relation(ENTITY_NAME3, ATTR_NAME1)))
                    .type(TYPE_ID2,
                            ENTITY_NAME2,
                            t -> t.attribute(
                                    ATTR_NAME1,
                                    REFERENCE,
                                    a -> a.relation(ENTITY_NAME3, ATTR_NAME1)))
                    .type(TYPE_ID3,
                            ENTITY_NAME3,
                            t -> t.attribute(
                                    ATTR_NAME1,
                                    REFERENCE,
                                    a -> a.relation(ENTITY_NAME2, ATTR_NAME1)))
                    .build());

        // Verify
        assertTrue(thrown.getMessage().contains("has inconsistent relation"));
    }

    @Test
    public void builder_same_relation_on_both_sides() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Model.Builder()
                    .type(TYPE_ID1,
                            ENTITY_NAME1,
                            t -> t
                                .attribute(
                                        ATTR_NAME1,
                                        REFERENCE,
                                        a -> a
                                            .relation(ENTITY_NAME2, ATTR_NAME2)
                                            .relation(ENTITY_NAME3, ATTR_NAME3)))
                    .type(TYPE_ID2,
                            ENTITY_NAME2,
                            t -> t
                                .attribute(
                                        ATTR_NAME2,
                                        REFERENCE,
                                        a -> a
                                            .relation(ENTITY_NAME1, ATTR_NAME1)
                                            .relation(ENTITY_NAME3, ATTR_NAME3)))
                    .type(TYPE_ID3,
                            ENTITY_NAME3,
                            t -> t
                                .attribute(
                                        ATTR_NAME3,
                                        REFERENCE,
                                        a -> a
                                            .relation(ENTITY_NAME1, ATTR_NAME1)
                                            .relation(ENTITY_NAME2, ATTR_NAME2)))
                    .build());

        // Verify
        assertTrue(thrown.getMessage().contains(" are present on both sides of the combined relation"));
    }

    @Test
    public void builder_different_relation_container_on_same_sides() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Model.Builder()
                    .type(TYPE_ID1,
                            ENTITY_NAME1,
                            (t) -> t
                                .attribute(
                                        ATTR_NAME1,
                                        REFERENCE,
                                        a -> a
                                            .relation(ENTITY_NAME2, ATTR_NAME2)
                                            .relation(ENTITY_NAME3, ATTR_NAME3)))
                    .type(TYPE_ID2,
                            ENTITY_NAME2,
                            t -> t.attribute(
                                    ATTR_NAME2,
                                    REFERENCE_LIST,
                                    ENTITY_NAME1,
                                    ATTR_NAME1))
                    .type(TYPE_ID3,
                            ENTITY_NAME3,
                            t -> t.attribute(
                                    ATTR_NAME3,
                                    REFERENCE_MAP,
                                    ENTITY_NAME1,
                                    ATTR_NAME1))
                    .build());

        // Verify
        assertTrue(thrown.getMessage().contains(" has different container type between reverse relations: "));
    }

    @Test
    public void equals_and_hash() {
        // Execute & Verify
        Asserts.assertEquality(MODEL1, MODEL2);

        Asserts.assertInequality(null, MODEL1, MODEL3);
    }
}
