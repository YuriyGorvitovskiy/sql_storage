package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.ValueType.INTEGER;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_LIST;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class Relation_UnitTest {
    static final long   TYPE_ID1     = 1;
    static final long   TYPE_ID2     = 2;
    static final long   TYPE_ID3     = 3;

    static final String ENTITY_NAME1 = "Entity";
    static final String ENTITY_NAME2 = "Hello";
    static final String ENTITY_NAME3 = "World";

    static final String ATTR_NAME1   = "first";
    static final String ATTR_NAME2   = "second";
    static final String ATTR_NAME3   = "third";

    final Model         MODEL        = new Model.Builder()
        .type(TYPE_ID1,
                ENTITY_NAME1,
                t -> t
                    .attribute(ATTR_NAME1, REFERENCE, ENTITY_NAME1, ATTR_NAME2)
                    .attribute(ATTR_NAME2, REFERENCE_LIST, ENTITY_NAME1, ATTR_NAME1))
        .type(TYPE_ID2,
                ENTITY_NAME2,
                t -> t
                    .attribute(ATTR_NAME1, INTEGER)
                    .attribute(ATTR_NAME2, INTEGER))
        .type(TYPE_ID3,
                ENTITY_NAME3,
                t -> t
                    .attribute(ATTR_NAME1, INTEGER)
                    .attribute(ATTR_NAME2, INTEGER))
        .build();

    @Test
    public void builder_direct() {
        // Setup
        final EntityType ENTITY = MODEL.getEntityType(ENTITY_NAME1);
        final Attribute  ATTR   = ENTITY.getAttribute(ATTR_NAME1);

        // Execute
        Relation relation = new Relation.Builder()
            .target(ENTITY_NAME1)
            .reverse(ATTR_NAME1)
            .build(MODEL);

        // Verify
        assertSame(ENTITY, relation.target);
        assertSame(ATTR, relation.reverse);
    }

    @Test
    public void builder_wrong_target() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Relation.Builder()
                    .target("NOT EXISTS")
                    .reverse(ATTR_NAME1)
                    .build(MODEL));

        // Verify
        assertTrue(thrown.getMessage().contains("doesn't exists"));

    }

    @Test
    public void builder_no_target() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Relation.Builder()
                    .reverse(ATTR_NAME1)
                    .build(MODEL));

        // Verify
        assertTrue(thrown.getMessage().contains("doesn't exists"));
    }

    @Test
    public void builder_wrong_reverse() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Relation.Builder()
                    .target(ENTITY_NAME1)
                    .reverse("NOT EXISTS")
                    .build(MODEL));

        // Verify
        assertTrue(thrown.getMessage().contains("doesn't exists"));
    }

    @Test
    public void builder_no_reverse() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Relation.Builder()
                    .target(ENTITY_NAME1)
                    .build(MODEL));

        // Verify
        assertTrue(thrown.getMessage().contains("doesn't exists"));
    }

    @Test
    public void builder_wrong_reverse_type() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Relation.Builder()
                    .target(ENTITY_NAME2)
                    .reverse(ATTR_NAME1)
                    .build(MODEL));

        // Verify
        assertTrue(thrown.getMessage().contains(" but should be " + Primitive.REFERENCE));
    }

    @Test
    public void equals_and_hash() {
        // Setup
        final EntityType ENTITY1           = MODEL.getEntityType(ENTITY_NAME2);
        final EntityType ENTITY2           = MODEL.getEntityType(ENTITY_NAME3);

        final Attribute  ENTITY1_ATTR1     = ENTITY1.getAttribute(ATTR_NAME1);
        final Attribute  ENTITY1_ATTR2     = ENTITY1.getAttribute(ATTR_NAME2);
        final Attribute  ENTITY2_ATTR1     = ENTITY1.getAttribute(ATTR_NAME1);
        final Attribute  ENTITY2_ATTR2     = ENTITY1.getAttribute(ATTR_NAME2);

        final Relation   TARGET_E1_A1      = new Relation(ENTITY1, ENTITY1_ATTR1);
        final Relation   TARGET_E1_A2      = new Relation(ENTITY1, ENTITY1_ATTR2);
        final Relation   TARGET_E2_A1      = new Relation(ENTITY2, ENTITY2_ATTR1);
        final Relation   TARGET_E2_A2      = new Relation(ENTITY2, ENTITY2_ATTR2);
        final Relation   TARGET_E1_A1_COPY = new Relation(ENTITY1, ENTITY1_ATTR1);

        // Execute & Verify
        Asserts.assertEquality(TARGET_E1_A1, TARGET_E1_A1_COPY);

        Asserts.assertInequality(null, TARGET_E1_A1, TARGET_E1_A2, TARGET_E2_A1, TARGET_E2_A2);
    }

}
